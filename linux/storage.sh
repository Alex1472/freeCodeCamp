# Summary
lsblk - information about block devices and paticular device
blkid - information about paticular block device. Use as super user.
fdisk - information about partitions and paticular partition 
df - information about file systems

# BLOCK DEVICES
# Block device is a type of file that can be gound under the /dev/ directory.
# It usually represents a piece of hardware that can store data: traditional
# spinning hard disk or solid-state disk(SSD) and other
# It is called block storage because data is read or written to it in blocks
# or chunks of space.
# To see block devices in your system
lsblk
sda      8:0    0 101.7G  0 disk 
├─sda1   8:1    0     1M  0 part /boot/efi
├─sda2   8:2    0  72.5G  0 part /media/MM/data
└─sda3   8:3    0  46.6G  0 part /
# or
ls -l /dev/ | grep "^b"
# sda represents an entire disk. sda1, sda2, sda3 are disk partitions.
# Each block device has a major and minor number(8:2). The major number(8)
# is used to identify a type of block device 8 - SCSI device, which has an
# fixed naming convention that starts with SD. This is the reason why the
# disk in the partition names start with SD. The minor numbers are used
# to distinguish individual, physical or logical devices. Which in this case
# identify the whole disk  and the partitions created.

# Use blkid to print block device attibutes
blkid /dev/sda 
# /dev/sda: PTUUID="edce9f44-4f45-4a67-ac2d-2e1a5e69937d" PTTYPE="gpt"



# PARTITIONS
# The entire disk can be broken into smaller segments of usable space called
# partitions. In example we have 3 partitions, represented by sda1, sda2 and
# sda3.
# The concept of partitions allows us to segment space and use each partition
# for a specific purpose.
# sda3 is used for the root partion.
# sda2 is mounted at /media/MM/Data, which is used to store backups in
# this system
# sda1 is used during the system boot process and contains the boot loaders
# for the installed OS.
# You don't necessarily have to partition a disk. It can be used as is
# without carving out partitions. But it is always recommended to partition
# a disk wherever possible as it offers great flexibility



# FDISK 
# The information about partitions is saved into a partition table.
# lsblk command is one way to read it
# another popular tool is fdisk
# It can be used to list the partition table information, to create and
# delete partitions.
# Print partition table
# fdisk - display or manipulate a disk partition table.
fdisk -l # display partitions
# the size of the disk in bytes and sectors.

# Or information about paticular partition'
fdisk -l /dev/sda
fdisk -l /dev/sda2



# PARTITION TYPES
# There are three types of disk partitions;
# - Primary partition - type of partition that can be used to boot an 
# operating system. Can't be more then 4 primary partitions per disk
# - Extended partition - type of partition that can't be use on its own
# but can host logical partitions. With the restriction of maximum four
# primary partitions, you can opt to create extended partitions and carve
# out logical partitions inside it. An extended partition is like a disk 
# drive in its onw right. It has a partition table that points to one or
# more logical partitions.
# - Logical partition - created within an extended partition.



# PARTITIONING SCHEME(TABLE), MBR, GPT
# freecodecamp.org/news/mbr-vs-gpt-whats-the-difference-between-an-mbr-partition-and-a-gpt-partition-solved/
# Before a drive can be divided into individual partitions, it needs to be 
# configured to use a specific partition scheme or table. A partition table 
# tells the operating system how the partitions and data on the drive are 
# organized.
# There are two main types of partition tables: MBR and GPT.

# In partition type section we discussed the MBR partitioning scheme.
# MBR - Master Boot Record
# It is been around for over 30 year now.
# With MBR, if we want more then 4 partitions per disk we should create 
# fourth partition as extended partition and carve out logical partitions within
# it. The maximum size per disc is two terabytes.
# GPT stands for GUID partition table and is a more recent partitioning 
# scheme, that was created to address the limitations in MBR. GPT can have an
# unlimited number of partitions per disc. This is usually only limited by
# the restrictions imposed by the operating system itself. The disc size
# limitation of two terabyte does not exest with GPT.
# Unless the operating system you want to install on the disc requires
# MBR, GPT is always the best choice.



# GDISK
# To manipulate with gpt partition table use gdisk
# gdisk - Interactive GUID partition table (GPT) manipulator.
# GPT  fdisk  (aka gdisk) is a text-mode menu-driven program for creation and
# manipulation of partition tables.
# To create partitions on the disk run gdisk and provide device path
gdisk /dev/sdb
# This will take you into a menu-drive interface.
# ? - print all available options
# n - create a new partition
# specify partition number, size, hex code for the partition type(stick with
# default 8300 code) l - to provide all possible values
# to provide size use format +500M (two times)
# The w - to write the partition
# Note, to show partition information you can use gdisk -l as well.




# FILE SYSTEMS
# Partitioning alone does not make a disk usable is OS. We must create 
# a filesystem. The filesystem defines how data is stored on a disk.
# After creating a filesystem we must mount it to a directory ant that's
# when we can read or write data to it.
# There are different file systems. Commonly used filesystems are
# the extended file system series for ext2 to ext4. 
# Comparasion:
#                  EXT2                EXT3                 EXT4
# Max file size    2 TB                2 TB                 16 TB
# Max volume size  4 TB                4 TB                 1 EX
#                  Long Crash Recovery Backwards Compatible Backwards Compatible
# Long Crash Recovery - long load after a crash
# Backwards Compatible - can be mount as EXT3, EXT2.



# CREATE FILE SYSTEMS
# To create ext4 file system run mkfs.ext4 command and pass device path
mkfs.ext4 /dev/sdb1
# Now mount the file system
mkdir /mnt/ext4
mount /dev/sdb1 /mnt/ext4
# We can check if the file system is mounted by running
mount | grep /dev/sdb1
# or
df -hP | grep /dev/sdb1
# To show file systems with their types use:
df -T

# To make the mount available after reboots, add an entry to the /etc/fstab
# <file system> <mount point> <type> <options>            <dump>  <pass>
echo "/dev/sdb1 /mnt/ext4 ext4 rm 0 0" >> /etc/fstab
# rm - options, allowed read and write fs
# first 0 - whether to backup the filesystem using the dump utility
# 0 - disable backup, 1 - take backup
# second 0 - priority set for the filesystem check tool to determine the
# order in which the file systems should be checked during a boot after a crach
# 0 means the tool will ignore the filesystem check



# EXTERNAL STORAGES
# For an enterprise-grade server environment such as a production database,
# or a web server we make use of high capacity external storage.
# Several types of storages:
# - DAS - direct attached storage
# - NAS - network attached storage
# - SAN - storage area network

# With DAS external storage is attached directly to the host system that
# requires the space. The host operating system sees a connected desk device
# as a block device. There is no network or firewall between the storage and 
# the host, which means this privide excellent performance at a very 
# affordable cost. DAS generally has a faster response than a NAS device.
# The downside is that since the DAS is directly attached, it is dedicated to 
# a single server. Therefore, this is not ideal for enterprise environments
# where multiple servers need storage and more suitable for small businesses.

# NAS is suitable for mid to large businesses. NAS storage device is generally
# located apart from the hosts that will consume space from it. The data 
# traffic between the storage and the host is through the network.
# NAS is a file storage device unlike DAS. The storage is provided to the 
# hosts in the form of a directory or a share that is physically present
# in the NAS device but exported via NFS to the hosts.
# This type of device is ideal for centralized shared storage that needs to
# be accessed simultaneously by several defferent hosts. The performance of 
# the NAS and high-speed ethernet connectivity between the two, it can
# provide good performance and highly available storage solution.
# This type of storage can be used as the back-end storage for web servers 
# and application servers and can run databases. NAS is not recommended
# storage to install operating systems.

# SAN provides block storage used by enterprises for business-critical
# applications that need to deliver high throughput and low latency.
# Storage is allocated to hosts in the form of a LUN - Logical Unit Number.
# A LUN is a range of blocks provisioned from a pool of shared storage and
# presented to the server as a logical disk. The host system will detect
# this storage as a raw disk. We can then create partitions and file systems
# on top of it and then mount it on the system to store data. 
# While SAN can also be ethernet-based, it mainly makes use of the FCP -
# Fiber Channel Protocol. This is a high-speed data transfer protocol that
# makes use of a fiber channel switch to establish communication with the 
# host. The host servers make use of HBA - Host Bus Adapter which is connected
# to the PCI slot to interface with the fier channel switch.
# The major advantage of SAN over NAS is that it can be used to host 
# mission-critical applications and databases due to its vastly superior
# performance and reliability. This include installing Oracle Databases or 
# Microsoft SQL DB 



# NFS
# Unlike the block devices, NFS doen't store data in blocks. In saves data
# in the form of files. NFS works on a server-client model. 
# Let's take the example of a software repository server. The directory 
# /software/repos exists on the repository server. This directory is then
# shared over the network using an NFS to the clients(for example, laptops).
# The data you see on your laptop may not physically reside on any of the
# attached disks. But once mounted, it can be used as any other file system
# in the operating system. The term for directory sharing in NFS is called
# exporting.
# How it works? The NFS server maintains an export configuration file at 
# /etc/exports that define the clients which should be able to access 
# the directories on the server. Assume the clients have a network IP of
# 10.61.35.201, 10.61.35.202, 10.61.35.203 and the NFS server has an IP of
# 10.61.112.101 . 
# /etc/exports sample:
/software/repos 10.61.35.201 10.61.35.202 10.61.35.203
# file to share and ips of clients to share to 

# In ideal situation, there would be a network firewall between the NFS
# server and the clients. As a result, specific ports may have to be opened
# between the server and the clients for the NFS solution to work.
# Once the /etc/exports file has been updated on the server, the directory
# is shared to the clients by using the exportfs command
exportfs -a # exports all the mounts defined in the /etc/exports file.
exportfs -o 10.61.35.201:/software/repos # manually export a directory
# Once it exported, you should be able to mount it on a local directory:
mount 10.61.112.101:/software/repos /mnt/software/repos
# ip of nfs server/folder to share on server



# LVM
# LVM - Logical Volume Manager
# LVM allows grouping of multiple physical volumes, which are hard disk or
# partitions into a volume group. From this volume group, you can carve out
# logical volumes. 
# For example, we can create from three partitions single volume group.
# There are several benefits of using LVM. 
# - LVM allows to logical volumes to be resized dynamically as long as 
# there is sufficient space in the volume group. 
# - You can have file systems created on top of LVM managed volumes.
# This allows them to be easily resized when needed. 

# To use LVM you should install LVM2 package
apt-get install lvm2
# 1) The first step in configuring LVM is to identify free disks or partitions
# then create physical volume objects for them. A physical volume object is
# how LVM identifies a disk or a partition. It is also called a PV.
pvcreate /dev/sdb # create a physical volume based on /dev/sdb disk
pvdisplay # show details about physical volumes
# 2) Then create a volume group(VG) based on physical volumes
# Volume group can have one or more physical volumes. 
vgcreate caleston_vg /dev/sdb # create a caleston_vg group with volume /dev/sdb
vgdisplay # show details about volume groups
vgs # list volume groups
# 3) Create a logical volume 
lvcreate -L 1G -n vol1 calestion_vg # create 1 GB logical volume, 
# in the kelston_vg group. -L - linear volume(type of logical volume) 
# enables to use multiple physical volumes if available in the volume 
# group to create a single logical volume.
lvdisplay # show details about logical volumes
lvs # list logical volumes 
# 4) Create a file system on logical volume
mkfs.ext4 /dev/caleston_vg/vol1 
# Note, logical volumes are at /dev/volume_group/logical_volumes
# 5) Mount the created file system
mount -t ext4 /dev/caleston_vg/vol1 /mnt/vol1 # -t to specify file type
# Now logical volume is available for use.
# 6) We can resize the file system on vol1 while it is mounted. First,
# we should check if there is enough space in the VG. 
# You can use it with vgs command and see free space
vgs
# Resize logical volume 
lvresize -L +1G -n /dev/caleston_vg/vol1 # increase on 1G, -L - specify size
# -n - specify logical volume
# Then we need to resize filesystem
resize2fs /dev/caleston_vg/vol1 
# Examine mounted file system size
df -hP /mnt/vol1

# Note, we didn't have to stop or unmount the file system while resizing. 
# Note, with df command we see that the file system is mounted at 
/dev/mapper/caleston_vg-vol1 # instead of 
/dev/caleston_vg/vol1
# Both paths are the same. The logical volume are accessible at two places
/dev/volume_group
/dev/mapper/volume_group-logical_volume











