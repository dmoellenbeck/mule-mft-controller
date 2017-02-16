package com.mulesoft.template.mft;


import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.mule.api.MuleMessage;

import org.mule.api.transformer.TransformerException;

import org.mule.transformer.AbstractMessageTransformer;

public class SftpPut extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		StandardFileSystemManager manager = new StandardFileSystemManager();

		String lpath = (String) message.getInvocationProperty("lPath");
		String fn =  (String) message.getInvocationProperty("fileName");
		String srv = (String) message.getInvocationProperty("server");
		String port = (String) message.getInvocationProperty("port");
		String username = (String) message.getInvocationProperty("username");
		String secret = (String) message.getInvocationProperty("secret");
		String rpath = (String) message.getInvocationProperty("rPath");

        File file = new File(lpath+"/"+fn);
        if (!file.exists())
            throw new RuntimeException("Error. Local file not found");

        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(createConnectionString(srv, username, secret, port, rpath+"/"+fn), createDefaultOptions());

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);

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
