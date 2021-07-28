# JServe
[![CI](https://github.com/kremi151/JServe/actions/workflows/ci.yml/badge.svg)](https://github.com/kremi151/JServe/actions/workflows/ci.yml)

A file server written in Java

## Compile JServe
1. Clone the Git repository:  
`git clone https://github.com/kremi151/JServe.git`
2. Build using Gradle:  
`gradle build`

## How to use JServe
### Serve files
You can run serve files using the following command:

`java -jar jserve-XXX.jar [options]`

### Create configuration file
You can create a configuration file with the following command:

`java -jar jserve-XXX.jar export [options] [--pretty] <target path>`

By specifying `--pretty`, the output JSON file will be pretty formatted.

### Command options
- `-d <path>`  
Specifies the path on the file system to be served.  
**Default**: The path relative to the executable
- `-p <port>`  
Specifies the port where the server should be reachable.  
**Default**: 1806
- `-m <max_threads>`  
Specifies the maximum number of threads to be used.  
**Default**: 10
- `-t <path>`
Specifies a comma separated values file of extension-to-mime-type mappings.  
**Default**: default (uses the integrated one)
- `-c <path>`  
Specifies the path of a JSON configuration file to load the configurations from.  
**Default**: *none*
- `-l <level>`  
Specifies the logging level to use.  
**Default**: _"info"_
