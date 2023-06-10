# CLASSIFICATION
# One of the common ways to categorize a Linux distribution is by the package
# manager it uses. Thera are to types:
# - rpm-based. RHEL(Red Hat), CentOS, Fedora
# - dpkg-based(dpkg - debian package). Ubuntu, Debian, Linux Mint

# Note, CentOS is a community-driven enterprise operation system, which is
# formed from Red Hat. Red Hat is available only through a paid subscription.



# PACKAGE
# Package is a compressed archive that contains all the files that are 
# required by our particular software to run. For example, for gimp we
# should get gimp.deb package. This package contains all the software binaries
# and files needed to the editor to run, along with metadata, which provides
# information about the software itself.

# Packages include a manifest of dependencies or list of programs and versions
# that must be satisfied for the packaged software to run correctly on a given computer.



# PACKAGE MANAGER
# A package manager is a software in a Linux system that provides a consistent
# and automated process of installing, upgrading, cofiguring and removing packages
# from the operating system.

# Depending upon the distribution, a Linux system supports several different
# types of package managers
# DPKG-BASED SYSTEM PACKAGE MANAGERS:
# - DPKG - the base package manager for Debian-based distributions
# - APT - a newer frontend for the dpkg system
# - APT-GET - is the draditional front-end for the dpkg system
# RPM-BASED SYSTEM PACKAGE MANAGERS:
# - RPM - the base package manager for Red hat-based distributions
# - YUM - a frontend for the rpm system
# - DNF - a more feature-rich front-end for the RPM system


# RPM 
# RPM - Red hat Package Manager(used in Red Hat, CentOS, Fedora)
# The file extension for packages managed by RPM is .rpm
# Install package, -i - install, -v - verbose
rpm -ivh telnet.rpm # install telnet.rpm package from current folder
# You can download and install package
rpm -ivh http://rpmfind.net/linux/opensuse/tumbleweed/repo/oss/x86_64/telnet-1.2-176.2.x86_64.rpm
# Uninstall
rpm -e telnet.rpm
# Upgrade
rpm -Uvh telnet.rpm
# RPM stores information about all installed packages in /war/lib/rpm
# Get information about the installed package
rpm -q telnet
# Verify the package
rpm -Vf <path to file> # is package from trusted and secured source
# NOTE, RPM DOESN'T RESOLVE PACKAGE DEPENDENCIES, SO YOU SHOULD Install
# ALL DEPENDENCIES MANUALLY


# YUM
# YUM = yellowdog updater, modified
# YUM works with software repositories, so you shouldn't install dependencies
# on your own.
# YUM depends on repositories from where it installs packages. Repository can
# be local or on a remote location with FTP or HTTP.
# The repository information is stored in the /etc/yum.repos.d directory and 
# repository files have a .repo extension. 
# Usually, the operation system comes bundled with its own software repository.
# You can add new .repo file to the directory to add new repository. For example:
/etc/yum.repos.d/nginx.repo
# To install packages, YUM uses RPM under the hood. 

# COMMANDS
# To install a package, enter yum install package-name. The yum check the 
# availability of the package and it dependencies in repos 
yum install httpd
# Then the result will be printed on the screen for user to review. If
# you want to proceed with the install enter y. Or you can provide the -y flag
yum intall httpd -y
# Show available repos
yum repolist
# Show what package provides specified command
yum provides scp # what package provides the scp command
# Remove a package
yum remove httpd
# Update a package
yum update httpd
# Update all packages in the system
yum update


# DPKG 
# dpkg - Debian package manager. It is similar to rpm.
# The package extension is .deb
# It is also doesn't works with repositories => you should install dependencies
# manually
# Install
dpkg -i telnet.deb # the package telnet.deb should be in the current folder
# Uninstall
dpkg -r telnet.deb
# List packages installed in the system
dpkg -l
# Check the status of the package
dpkg -s telnet
# Display detail about the package
dpkg -p telnet


# APT 
# apt - advanced package manager
# High level package manager.
# Relies on dpkg under the hood.
# Works with dependencies.
# Software repositories for apt is defined in the /etc/apt/sources.list file
# It can be local or remote.
# Update information about packages in all available sources.
apt update
# Install update for all packages currently installed in the system.
apt upgrade
# Update repository list, then text editor will be opened and you can exit the file
apt edit-sources # edit /etc/apt/sources.list
# Install a package
apt install telnet
# Remove a package
apt remove telnet
# Look for a package in repositories
apt search telnet # or
apt list | grep telnet # apt list - show all available packages

# APT is a more user freindly tool the APT-GET
# APT provides output information in a more user-friendly format.
 
