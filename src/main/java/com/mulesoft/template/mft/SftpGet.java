package com.mulesoft.template.mft;


import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.mule.api.MuleMessage;

import org.mule.api.transformer.TransformerException;

import org.mule.transformer.AbstractMessageTransformer;

public class SftpGet extends AbstractMessageTransformer {
	

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		StandardFileSystemManager manager = new StandardFileSystemManager();

		String fn =  (String) message.getInvocationProperty("fileName");
		String srv = (String) message.getInvocationProperty("server");
		String port = (String) message.getInvocationProperty("port");
		String username = (String) message.getInvocationProperty("username");
		String secret = (String) message.getInvocationProperty("secret");
		String lPath = (String) message.getInvocationProperty("lPath");
		String rPath = (String) message.getInvocationProperty("rPath");
		String nodeName = (String) message.getInvocationProperty("nodeName");

		try {
			manager.init();

			FileObject localFile = manager
					.resolveFile(lPath + "/"+ nodeName + "/" + fn);

			String remoteFilePath = rPath + "/" + fn;

			FileObject remoteFile = manager.resolveFile(
					createConnectionString(srv, username, secret, port, 
							remoteFilePath), createDefaultOptions());

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);

			
			// Move file to archive
			FileObject dir = manager.resolveFile(createConnectionString(srv, username, secret, port,  rPath + "/archive"), createDefaultOptions());
			FileObject archiveFile = manager.resolveFile(createConnectionString(srv, username, secret, port,  rPath + "/archive/" + fn), createDefaultOptions());

			
            if (remoteFile.exists()) {
            	dir.createFolder();
                remoteFile.moveTo(archiveFile);
            }
            
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
		
		return null;
	}

	public static String createConnectionString(String hostName,
			String username, String password, String port, String remoteFilePath ) {
		return "sftp://" + username + ":" + password + "@" + hostName + ":" + port + "/"
				+ remoteFilePath;
	}

	public static FileSystemOptions createDefaultOptions()
			throws FileSystemException {

		FileSystemOptions opts = new FileSystemOptions();

		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		return opts;
	}

}
