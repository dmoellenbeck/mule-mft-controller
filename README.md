# mule-mft-controller
Sample mule template with APIs that can be used to communicate with any SFTP server for basic Managed File Transfer operations.

It can be protected by API Manager to restrict the access to the exposed operations, for instance controlling the remote site registration via client_id/client_secret.

It is compatible with the [mule-mft-agent], a pre-configured mule-application with a built-in SFTPD server that can facilitate the registration process as well as "instant-push" capability for immediate file transfer initiated in the server hosting the agent.

It currently used ObjectStore as the persistence store, but it can be customized easily if required.

### Version
0.2
### Description
This template, after configuration can be used as a System API to :
- Initiate a file transfer (2 ways, controller <--> remote site) 
- Schedule a fie transfer
- Replay a transfer if it fails
- Upload a file 

In addition it also also expose some basic functions such as:
- Registration/Unregistration of a remote site
- List Remote site (nodes) information
- List / Cancel / Delete All Jobs
- List failed File transfer transaction (for replay)
- List all pending transactions
- List / Delete all completed transactions logs

### Configuration
After importing the template into Anypoint Studio and add all the ***.jar** in the **resources** folder into the project **Build Path**, configure the properties file to match your environment. Below is an example of the controller properties file.
```sh
# mft controller setting
# ======================

# mft controller api port
mft.port=8091

# mft controller api baseuri
mft.basePath=/mft/*

# Root Directory used by the controller and agent for files exchange
mft.root=/Users/mule/Desktop/mft/controller

# Directory (relative to Root Directory) where all files 
# pushed by the agent will be saved
mft.agent.upload.dir=/inbox

# Directory (relative to Root Directory) where all files 
# upload from api will be saved
mft.web.upload.dir=/web

# Controller Timezone (ex: +0800 for Hong Kong)
mft.timezone.offset=+0800

# Objectstore persistence setting
# ===============================

# Store used to persist all agent metadata 
store.metadata.persistence=true

# Store used to persist all pending tasks (used for replay)
# Recommend to set to "false" during testing
store.current.persistence=false

# Store used to persist all error logs (used for replay)
# Recommend to set to "false" during testing
store.error.persistence=false

# Store used to persist all completed tx logs (used for replay)
# Recommend to set to "false" during testing
store.completed.persistence=false

# Store used to persist all jobs
# Recommend to set to "false" during testing
store.jobs.persistence=false
```



   [mule-mft-agent]: <https://github.com/mulesoft-consulting/mule-mft-agent>
   

