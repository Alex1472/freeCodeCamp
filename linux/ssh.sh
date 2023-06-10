#SSH
# SSH is used for logging into and executing commands on a remote
# computer
# Syntax is:
ssh <hostname OR IP Address> # by default in tries to login on remote machine
ssh devapp01 # as a current user, devapp01 hostname and enter password
# as user you login on your machine
# Also you can pass a user you want to connect
ssh <user>@<hostname OR IP Address>
# or
ssh -l <user> <hostname OR IP Address>

# The remote server however should have an SSH service running in port 22
# accessible from the client for the connection to work
# Another requirement valid login and password created on the remote system
# or you can use SSH key to login to remote machine without a password



# PASSWORD-LESS SSH
# First we need to generate a key pair on the client.
# A key pair consists of PRIVATE KEY and PUBLIC KEY.
# PRIVATE KEY is a ke only you as a the client will have and is not shared
# with anyone else.
# PUBLIC KEY is a public and can be shared with others including out remote 
# server.
# When the public key is installed on the remote server, you can unlock it
# by connecting to it with a client that already has the private key.

# 1) First we need to create the key pair on the client with SSH-KEYGEN command
#    It will ask you to enter passphrase, this is optional, but impoves
#    the security of the key should it become compromised. The only downside
#    to having a passphrase, is then having to type it in each time you use
#    the key pair.
#    The public key is stored in ~/.ssh/id_rsa.pub
#    The private key is stored in ~/.ssh/id_rsa
ssh-keygen -t rsa
# 2) Copy the public key to the remote server. You will have to resort to 
# password-based authentication at least once. Easy way is to use command
# ssh-copy-id. The public keys are installed on a server in the 
# ~/.ssh/authorized_keys file (you can see it there)
ssh-copy-id bob@devapp01 # and enter password
# After that you will be able to access the remote server without entering
# a password
# Then you can use just to enter
ssh devapp01

# SCP
# The scp command allows you to copy data over SSH. This means you can copy
# files and directories between your laptop and any other server to which
# you have SSH access.
# As long as you can SSH to the web server, you can copy the files like this.
scp /home/bob/caleston-code.tar.gz devapp01:/home/bob # copy from local 
# machine to the server
# Note, you should to have permission to write to the directory on the 
# destination
# If you want to copy directories instead of files, use the -r option
# To preserve the ownershhip and permission of the source file, make use 
# of -p flag
scp -pr /home/bob/media devapp01:/home/bob



