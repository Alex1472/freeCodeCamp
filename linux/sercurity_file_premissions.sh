# ACCOUNTS
# Every user in linux has an assosiated ACCOUNT. The user account maitains
# information such as the username and password. Besides this, a user account
# also contains an identifier called UID, which is unique for each user in
# the system. The information about a user account is stored in /ETC/PASSWD
# file.
# A linux GROUP is a collection of users. It is used to organize users based
# on common attributes such as role or a function. The information about
# groups is stored in the /ETC/GROUP file. Each group has a unique identifier
# called the GID.

# User account stores:
# - username - michael
# - UID - 1000
# - GID - 1000 id of the group they are part of. A user can be part of 
# multiple groups. If no group is specified when the user is created,
# it assigns the user to a group with the same ID and name as the user ID
# That's the primary GID of the user.
# - home directory - /home/michael
# - default shell - /bin/shell
# You can check the information about a current user with id command:
id
uid=1000(alex147) gid=1000(alex147) groups=1000(alex147),4(adm),24(cdrom),
27(sudo),30(dip),46(plugdev),122(lpadmin),135(lxd),136(sambashare)
# Or you can specify username for another user
id alex147
uid=1000(alex147) gid=1000(alex147) groups=1000(alex147),4(adm),24(cdrom),
27(sudo),30(dip),46(plugdev),122(lpadmin),135(lxd),136(sambashare)
# You also can check the /etc/passwd file
alex147:x:1000:1000:Alex147,,,:/home/alex147:/bin/bash

# There are sevearal types of accounts:
# - User account - individual people who need access to the Linux system
# - Super user account is the root which has the UID 0. The superuser has
# unrestricted control over the system including other users.
# - System accounts - UID < 100 || 500 < UID < 1000. They are created during
# the OS installation. These are for use by software and services
# that will not run as the superuser. They usually don't have a dedicated
# home directory. Example: SSHD, mail user.
# - Service accounts - are similar to system accounts and are created
# when services are installed in Linux. For example, an nginx service makes
# use of a service account called nginx.

# WHO
# Use the command who show currently logged in users
who
alex147  pts/1        2023-05-27 21:42

# LAST 
# The last command shows a listing of last logged in users, it also shows
# when the system wa rebooted.
last
alex147  tty2         tty2             Wed Mar  8 22:14 - crash  (00:02)
reboot   system boot  5.19.0-35-generi Wed Mar  8 22:14 - 22:21 (60+00:07)
alex147  tty2         tty2             Wed Mar  8 22:01 - down   (00:12)
reboot   system boot  5.19.0-35-generi Wed Mar  8 22:00 - 22:14  (00:13)



# SWITCHING USERS
# SU (switch user)
# With su command you can switch to any other user in the system.
su alex147
# By default you try to switch to superuser
su # switch to the superuser
# Also you can run a specific command from specified user with the -c option.
# But it is not recommended because you need to enter password
su -c "whoami" alex147 # alex147, specify what command and username
# By default from super user
su -f "whoami" #root

# SUDO
# SUDO command offers another approach to giving users administrative access.
# When trusted user precede an administrative command with sudo, they are
# prompted for THEIR OWN PASSWORD. The default configuration for sudo 
# is defined under /etc/sudoers file. This file defines the policies applied 
# by the sudo command. Only users listed in the /etc/sudoers file can
# make use of the sudo command for privilege escalation.
# The administrator can give granual level of access with sudo. One user
# can execute one commands, other - another commands.



# SUDOERS FILE
# In this file administrator can specify privileges with sudo command for
# users and groups
# Lets look at the syntax. 
# A line has structure
# 1) User or group: bob, %sudo - for groups use %, here sudo group
# 2) Host: in which the user can make use of privilege escalation. 
#    By default ALL. Usually there is only one host - localhost.
# 3) Users and groups the user in the first field can run the command as.
#    Default value - ALL, ALL. e. c any user not just root can be used to
#    run the commands
# 4) The command, that can be run. You can specify individual commands or all
# For example:
sarah localhost=/usr/bin/shutdown -r now # sarah is allow only shutdown the system
bob ALL=(ALL:ALL) ALL # bob is allow execute any commands
%sudo	ALL=(ALL:ALL) ALL # anyone from the group sudo can execute any commands



# MANAGING USERS
# To create a new local user in the system use command useradd:
useradd bob # specify the name of the user
# You can specify some options for user:
# -c - Custom comment
# -d - Custom home directory
# -e - Expiry date
# -g - specific GID
# -G - create user with multiple secondary groups
# -s - Custom shell
# -u - specific UID
# Example, specify custom uid, gid, directory and shell
useradd -u 1003 -g 1001 -d /home/jack1 -s /bin/sh  jack
# To set new password use the command passwd
passwd bob # and enter old and new password. 
# User can change thier own password
passwd 
# Delete user
userdel bob

# MANAGING GROUPS
# GROUPADD
# Create a group. With the -g flag, you can specify gid
groupadd -g 1011 developers
# GROUPDEL
# Delete a group
groupdel developers



# ACCESS CONTROL FILES
# Most of the access control files are stored under the /etc directory.
# It is readonly for all users, but root user has access to write to it.
# The access control fils are designed in a way that they should never be
# modified using a text editor, use the built-in commands.
# Basic access control files in linux:
# - /etc/passwd - contains basic information about the users in the system.
# This file doesn't save any passwords. Also known as PASSWORD file. 
# - /etc/shadow - stores user passwords. The contents of this file are hashed.
# - /etc/group - stores information about all groups on the system. It stores
# group name, GID and memebers.

# Each line in this files represents information about particular user or a 
# group. These lines have multiple fields that are delimited by a colon.

# For the password file(/etc/passwd) the fields separated are:
USERNAME:PASSWORD:UID:GID:GECOS:HOMEDIR:SHELL
alex147:x:1000:1000:Alex147,,,:/home/alex147:/bin/bash
# The password field is X because it is located in the /etc/shadow file.
# Gecos is a CSV format of user information such as full name, location, 
# phone number ... . It can be blank.

# Let us look at the /etc/shadow file.
# It stores password of the users.
# The fields of the file:
USERNAME:PASSWORD:LASTCHANGE:MINAGE:MAXAGE:WARN:INACTIVE:EXPDATE
alex147:$y$j9T$8KHIQYb0ESbpKxR9iIyhG1$1XSWIFXv7siTokU0Q3cadH2khj8NlQDuBhz8U
Gc7nm/:19424:0:99999:7:::
# PASSWORD is a field cantaining the encrypted password.
# LASTCHANGE is the last changed, which is the date since the password was
# last changed. This value is in epoch which is a Unix timestamp described
# as the number of days that have elapsed since midnight January 1st, 1970.
# MINAGE AND MAXAGE are the minimum and maximum age. These describe 
# the minimum number of days the user will have to wait before being allowed
# to change the password again and the maximum number of days before which the
# user will have to change the password.
# WARN is the number of days before a password is going to expire that the
# user should be warned so that they can chage the password.
# INACTIVE is the number of days after a password has expired that it should
# still be accepted before being disabled.
# EXPDATE is the expiry date of the password. It is in epoch. Can be empty

# Next is an /etc/group file.
# It stores information about groups.
# It is format is
NAME:PASSWORD:GID:MEMBERS
sudo:x:27:alex147
# PASSWORD usually is x, indicating that this is saved in the shadow file.
# MEMBERS - members of the group.



# LINUX FILE PERMISSIONS
# With ls -l command you can determine the thpe of file, file permissions
-rwxrwxr-x
# THE FIRST SYMBOL indicates the file type:
# d - directory
# - - regular file
# c - character
# | - link
# s - socket file
# p - pipe
# b - block file

# Last 9 characters describe file permissions
# These character can be broken down as the OWNER PERMISSIONS, GROUP 
# PERMISSIONS and PERMISSIONS FOR OTHERS.
# THE FIRST THERE CHARACTERS are the permissions for the owner of the file
# denoted by the letter u.
# THE NEXT THERE CHARACTERS are permissions for the group owning the file
# denoted by g.
# THE LAST THERE CHARACTERS are permissions for the all other users,
# denoted by o.

# When an r bit is set the respective user has read permissions to the file.
# It has an octal value of 4 (we see it later)
# Bit         Purpose              Octal Value
# r           Read                 4
# w           Write                2
# x           Execute              1
# -           No permission        0

# For directories x - user can read from directory, w - can write in the
# directory, 
# In Linux the system identifies the user trying to access the file or
# directory and checks the perissions sequentially. If the user trying to 
# access is the owner, the owner's permissions for the file are applied
# and the rest is ignored. If the user is not the owner but part of the 
# owning group, only the group permissions are applied. The others are ignored.

# The rwx permissions can be represented by athree digit numeric value in 
# octal notation. So
# rwx - 7
# r-x - 5
# -wx - 3

# CHMOD
# The command used to modify the permissions of a file - chmod. 
# You should specify permission and file
# Permission can be modified in 2 way - SYMBOLIC and NUMERIC
# In SYMBOLIC mode, 
# The first characters specify whose permission you want
# to alter. u - user, g - group, o - others. You can use several values
# After these characters, you need to specify if you want to grant access
# with + symbol or revoke existing acces with - symbol. The specify permissions
# grant or revoke
chmod u+rwx test-file # grant user rwx permission
chmod ugo+r-x test-file # grant user, group, others read and write permission
chmod o-rwx test-file # revoke from others read, write and execute permissions
chmod u+rwx,g+r-x,o-rwx test-file # for user grant rwx, for group grant rx,
# for other revoke rwx permissions
# In NUMERIC mode you should specify three digit octal values.
# The first digit is permissions for owner, second - for group, 
# third - for others.
chmod 777 test-file # give all permission for owner, group and others
chmod 555 test-file # all can read and execute
chmod 660 test-file # owner and group can read and write
chmod 750 test-file # owner - all access, group - read and execute

# MODIFIYING FILE PERMISSIONS
# Every file has and owner and owner group. 
# You can change them.
---x-w-r--  1 root    root      47 Մայ 28 22:07 text.txt
#             owner   group
# You can change owner and group with the command chown
# You should specify owner:group and file
chown alex147:alex147 text.txt
# You can change only owner 
chown alex147 text.txt
# You can change only group with the command chgrp. You should specify 
# group and file
chgrp developer text.txt









