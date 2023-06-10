# USE TAB TO COMPLETE COMMAND, ARGUMENT, OPTION, ...

# The linux shell is a program that allows text-based interaction 
# between the user and the operating system.

# HOME DIRECORY
# The home directory allows users to store their persenal data in the form
# of files and directories
# Each user in the system gets their own unique home directory.
# Other users can't access your files or folders within your home directory.
# IT IS REPRESENTED BY ~ IN COMMAND LINE.

# Commands
# Command has arguments (input data) and option or switch or flag 
# (that specifies it behaiour)
# You should enter options with - or --.
echo Hello # Hello - argument

# Commands can be categorized into two types: internal(built-in) and external.
# Internal commands a part of the shell itself. There are in total about 30 such commands
echo, cd, pwd, set
# External commands are binary programs or scripts which are usually located
# in distinct files in system.
# They are can be created and installed by user.
# To determine is the command internal or external use the TYPE command
type echo # echo is a shell builtin
type mv # mv is /usr/bin/mv

/ # root directory



# There are several types of shell in linux:
# - Bournce Shell (sh)
# - C Shell (csh or tcsh)
# - Korn Shell (ksh)
# - Z Shell (zsh)
# - Bournce again Shell (bash)

# Show awailable shells
cat /etc/shells
# To change default shell use command chsh and print path to the shell
chsh 
# end enter new shell (from cat)
/bin/sh # you should log out to see the changes

# You can use alies command to set sudonim to command
alias dt=date
dt # Sat May 27 01:55:51 AM +04 2023



# ENVIRONMENT VARIALBLE
# Environment variables store information about the user's login session,
# which is used by the shell when executing commands
# use the ENV command to show all env variables
env
# To add env variable use command export. It add specified variable for the 
# current shell
export a=1
# To set new value to a variable just specify variable and value
a=42
# To make env variable presistent for subsequent logins add them into files
~/.profile # or
~/.pam_environment 
# .profile
a=5
export a



# PATH VARIABLE
# Shell search external commands in paths specified in the PATH variable
echo $PATH
# To check the command location use which command
which nano
# To add path to the path variable use export(during terminal live)
# And concatinate new path with current value
export PATH=$PATH:/opt/obs/bin


# BASH PROMPT 
# The line you see in a terminal is called bash prompt: alex147@alex147-VirtualBox:~$
# It can provide meaningful information about the logged in user, name of the system
# and the location on the system.
# The bash propmt is set and controlled by a set of environment variables.
# The most important is PS1. You can customize it by using special backslash
# escape special characters:
# - \d - show data
# - \t - show time
# - \h - show host name
# - \w - show current directory
# For example:
PS1="[\d \t] \w:" # [Sat May 27 12:23:27] ~:
# You can customize color of the command prompt. You should wrap you value
# with "\e[x;ym ... \e[m" where x, y color parameters to specify
# For example 
PS1="\e[01;32m[\d \t] \w: \e[m"
# For permanent changing you should change the value in the ~\.bashrc file
