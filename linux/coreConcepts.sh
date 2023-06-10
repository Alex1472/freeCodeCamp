# Kernal is a major component of the operating system and it is the core interface
# between a computer's hardware(CPU, memory, ...) and and its process. It communicates between
# the two managing resources as efficiently as possible.
# If we use anolagy:
# Library - operating system
# Books - resources
# Students - application processes
# Librarian - kernal
# Operating system manages resources for processes.

# The kernal is responsible for four major tasks:
# - MEMORY MANAGEMENT - keep track of how much memory is used to store 
# what and where
# - PROCESS MANAGEMENT - determine which processes can use the CPU when
# and for how long.
# - DEVICE DRIVERS - act as a mediator or an interpreter between the hardware 
# and processes
# - SYSTEM CALLS AND SECURITY - receive request for service from the processes

# The kernal is monolithic, this means that the kernal carries out CPU scheduling
# memory management and several other operations by itself.
# The kernal is modular, this means it can extend its capabilities through the 
# use of dynamic loaded kernel modules.

# Use uname command to display information about the kernel
uname # Linux
# To print the kernal version use -r flag:
uname -r # 5.19.0-41-generic
# 5 - kernel version
# 19 - major version
# 0 - minor version
# 72 - patch release
# generic - distro specific info 
# ANY LINUX DISTRIBUTIONS MAKE USE OF ANY ONE VERSION OF THESE VERSIONS OF KERNEL
# kernel.org - open-source project that hosts the repositories that make all
# versions of the kernel source code anaailable to all users.

# Memory is divided into two areas: kernel space(kernel) and user space(user mode)
# Kernel space is a portion of memory in which the kernel executes and 
# provide its services. A process running in the kernel space has unrestricted
# access to the hardware. It is strictly reserved for running the kernel code
# kernel extentions and most device drivers. Kernel space - section of the library
# that only the librarian or administrators of the library have access to.
# All processes running outside the kernel reside in the user space which
# has restricted access to CPU and memory (its a hall in the library)
# All usual programs are user-space applications

# User programs access the data by making special requests to the kernel 
# called system calls. Examples including allocatig memory by using variables 
# or opening a file (its a request to the librarian)



# 32-BIT VS 64-BIT PROCESSORS
# A 32-bit system can access 232 different memory addresses, i.e 4 GB of RAM 
# or physical memory ideally, it can access more than 4 GB of RAM also. 
# A 64-bit system can access 264 different memory addresses, i.e actually 
# 18-Quintillion bytes of RAM. In short, any amount of memory greater 
# than 4 GB can be easily handled by it.
# Most computers made in the 1990s and early 2000s were 32-bit machines. 
# The CPU register stores memory addresses, which is how the processor 
# accesses data from RAM. One bit in the register can reference an individual 
# byte in memory, so a 32-bit system can address a maximum of 
# 4 GB (4,294,967,296 bytes) of RAM.
# The register is generally used to load sata form memory and carry out
# arithmetic operations.
# With this difference in architecture also comes the limitation of
# software running on them. A machine with a 32-bit CPU architecture can only
# run a 32-bit OS, but the other way around is still possible(run 32-bit OS on
# 64-bit CPU). The same is applicable for software running on 
# these machines
# In order for data or a program to run it needs to be loaded into RAM first,
# so the data stored on the slower hard drive and from hard drive it's loaded
# into the faster RAM, and once it's loaded into RAM, the CPU can now access 
# the data or run the program. In now in a 32-bit system, since the maximum
# amount of memory that it can support is 4 GB, it may not be enough to hold
# all the data that the CPU needs to make the computer run as fast as possible
# and when this happens then some of the data has to be kept on the slower
# hard drive to compensate for the low memory. So instead of data going from RAM
# to CPU, it has to do extra work by going back to the slower hard drive, 
# load the nessesary data into RAM and then CPU can handle it.
# When this happends it slows down the computer.



# USEFULL COMMANDS
# Let's take an example of USB disk to be used in the system.
# As soon as the device is attached to the system, a corresponding 
# device driver, which is part of the kernel space, detects the stage change, 
# and generates an event. This event, which is called uevent is then sent 
# to the user-space device manager daemon called UDEV. The udev service is then
# responsible for dynamically creating a device node associated with the newly
# attached drive in the /dev file system. Once this process is complete,
# the newly attached disk should be visible under /dev file system.

# RING BUFFER, DMESG
# To avoid losing notable error messages and warnings from this phase of 
# initialization, the kernel contains a ring buffer that it uses as a message 
# store. The kernel ring buffer stores information such as the initialization messages 
# of device drivers, messages from hardware, and messages from kernel modules.
# These messages contain logs from the hardware devices that the kernel detects
# and provide a good indication of whether it is able to configure them.
# dmesg is a tool used to display message from the ring buffer
dmesg

# UDEV, UDEVADM
# Udev is the Linux subsystem that supplies your computer with device events. 
# In plain English, that means it's the code that detects when you have 
# things plugged into your computer, like a network card, external hard 
# drives (including USB thumb drives), mouses, keyboards, joysticks and 
# gamepads, DVD-ROM drives, and so on.
# The udevadm utility is a management tool for udev. 
# The udevadm info command queries the udev database for device information
udevadm info --name=/dev/sda1
udevadm info --query=path --name=/dev/sda1
# The udevadm monitor command listends to the kernel uevents and prints 
# the details, such as the devicepath and device name on the screen.
# This command is handy to determine the details of a newly attached or 
# removed device.
udevadm monitor

# LSPCI
# The lspci is used to display information about all PCI devices that are
# configured in the system: ethernet cards, raid controllers, video cards and
# wireless adapters that directly attach to PCI slots tin the motherboard
# of the computer 
lspci

# LSBLK
# The lsblk command lists information about block devices. Here sda is 
# the physical disk. sda1, sda2, ... refers to the partition.
# Note, disk refers to the whole physical DISK, and the PARTITION refers
# to a reusable disk space carved out of the physical disk.
# There is a major and minor number assosiated with each disk and partition.
# The major number identifies the type of device driver associated with 
# the device. The minor number is used to differentiate amongst devices
# that are similar and share the same major number.
sda1 8:1
# Some commonly used devices along with their major numbers
# 1 - RAM
# 3 - HARD DISK or CD ROM
# 6 - PARALLEL PRINTERS
# 8 - SCSI DISK

# LSCPU 
# To display information about the CPU architecture, use the lscpu command
lscpu
Architecture:            x86_64 
Socket(s):           1 # physical slots on the motherboard where you can 
# insert a physical CPU
Core(s) per socket:  2 # each physical CPU can have multiply cores, in this
# case the number of cores is 2
Thread(s) per core:  1 # each core can run multiple thread in the same time
# in this case 1
# The total number of CPU or virtual CPUs rather available on the system
# is the number of sockets multiplied by the number of cores and threads,
# which is 1 * 2 * 1 = 2 in this case
CPU(s):                  2 # sockets * cores * threads

# LSMEM, FREE
# The lsmeme command can be used to list the available memory in the system.
lsmem --summary
Memory block size:       128M
Total online memory:    10.5G # it has 10.5G total RAM memory
Total offline memory:      0B
# More comprehensive output
lsmem
# Another command to see information about the memory is the free command.
# This command shows the total versus used memory in the system
free -m # -m show memory in MB
               total        used        free      shared  buff/cache   available
Mem:           10457        1491        4943          95        4022        8599
Swap:           2047           0        2047

# LSHW
# The lshw is a tool to extract detailed information on the entire hardware 
# configuration of the machine
lshw # hardware lister



# LINUX BOOT SEQUENCE
# The boot process can be broken down into four stages:
# 1) BIOS POST It has very little to do with Linux itself. POST - power-on
#    self-test. In this stage, BIOS runs a POST test to ensure that the hardware
#    components attached to the device are finctioning correctly.
#    If POST fails, the computer may not be operable. The boot process interrupts.
# 2) Boot loader(GRUB2) The BIOS loads and executes the boot code from the 
#    boot device, which is located in the first sector of the hard disk.
#    In Linux, this is located in the /Boot File System. The boot loader
#    provides the user with the boot screen(select operating system to load).
#    Once the selection is made, the boot loader loads the kernel into memory
#    and hands over the control to the kernel.
# 3) Kernal initialization
#    Kernel starts executing. The kernel carries out tasks such as initializing
#    hardware and memory management tasks. The the kernal looks for the initialization
#    process to run.
# 4) The service initialization  using systemd.
#    The init function is called the SYSTEMD daemon. The systemd is responsible
#    for bringing the Linux host to a usable state. Systemd is responsible
#    for mounting file systems, starting and managing system services.
#    It sets up the user space and the processes needed for the user environment.

# To check the init system used, run
ls -l /sbin/init
# rwxrwxrwx 1 root root 20 Մար  2 16:58 /sbin/init -> /lib/systemd/systemd



# RUNLEVELS
# Linux can run in multiple modes:
# - graphical mode 
# - nongraphical mode
# - ...

# The mode can set by something called the RUNLEVEL.
# To see the operation mode set in the system, run the command
runlevel # N 5 - runlevel is 3

# The operation mode provides graphical interface is called the run level 5.
# The runlevel 3 implies a nongraphical mode.
# During boot the init process checks the run level.
# It makes sure that all programs needed to get the system operational
# in that mode are started.
# For example, the graphical user mode requires a display manager service
# to run for the GUI to work. This service is not required for the nongraphical mode.

# In systemd runlevels are called targets.
# Runlevel  Systemd             Function
# 5         graphical.target    Boots into a graphical interface
# 3         multiuser.target    Boots into a command line interface
# There are other runlevels and targets in use. 
# runlevel 0 -> poweroff.target
# runlevel 1 -> rescue.target
# runlevel 2 -> multi-user.target
# runlevel 3 -> multi-user.target
# runlevel 4 -> multi-user.target
# runlevel 5 -> graphical.target
# runlevel 6 -> reboot.target

# SYSTEMCTL
# The command systemctl is a tool to control the init system. It controls
# systemd and service manager.
# To see the default target use:
systemctl get-default
# To change the default target use:
systemctl set-default new-target
systemctl set-dafault multi-user.target



# FILE TYPES
# Everything is a file in Linux, every object in Linux can be considered
# to be a type of file.
# There are 3 types of files:
# - REGULAR FILES Text Images, Scripts, Configuration / Data files
# - DIRECTORY
# - SPECIAL FILE, can be subcategorized into five other file types
#   - CHARACTER FILE - represent devices under the /dev file system
#     that allows the OS to communicate wo IO device. For example mouses
#     keyboard
#   - BLOCK FILE, these files represent block device also located under /dev
#     Use lsblk commands to see. A block device reads from and write to the device
#     in blocks or a chunk of data. For example, hard disks and RAM.
#   - LINKS, It is a way to associate two or more file names to the same set 
#     of file data. There are two types of link:
#     - HARD LINK associate two or more file names that share the same bclok
#       of data on the physical disk. Deleting on link will delete the data.
#     - SOFT LINK can be loosely compared to a shortcut in Windows. It act
#       as a pointer to another file. Deleting a symbolik link does not
#       affect the data in the actual file.
#     - SOCKET is a special file that enables the communication between
#       two processes.
#     - NAMED PIPES allow connecting one process as an input to another.
#       The data flow in a pipe is unidirectional from the first process
#       to the second.

# To identify different file types use the file command:
file loop7 # loop7: block special (7/7)
file get-docker.sh # get-docker.sh: POSIX shell script, ASCII text executable
# Also you can identify the type of file by using ls command
ls -l # First charcter in the output mean file type
drwxr-xr-x  2 alex147 alex147  4096 Mar  8 22:01 Documents # d - directory
# Other types
# d - directory
# - - regular file
# c - character device
# | - link
# s - socket file
# p - pipe
# b - block device



# FILE HIERARCHY
# - /home - is the location that contains the home directories for all users 
# except for the root user
# - /root - is the location of the home directory for the root user`
# - /opt - place for third-party programs
# - /tmp - place for temporary files
# - /mnt - is used to mount file systems temporarily in the system
# It is used for temporary mounting. We mount here by yourself.
# - /media - all external media is mounted under the /media file system
# It is used for mounting REMOVABLE devices(usb). System mounts here.
# - /dev - contains the block and character device files. This directory
# contains files for devices such as external hard disks and devices
# such as the mouse and keyboards.
# - /bin - contains the basic programs and binaries such as the cp, mv, ...
# - /etc - is used to store most of configuration files 
# - /lib, /lib64 is the place to look for shared libraries to be imported
# into your programs
# - /var - system writes logs and cached data here.

# The df command prints out details about all the mounted file systems.
df



