# >, >>
# You can redirect command output to file not to terminal.
# > - overwrite the file
# >> - append output to file
echo Hello bash > stdout.txt
echo Hello bash >> stdout.txt



# Redirect nothing to file, just clean it.
> stdout.txt

# 1>, 2>
# There’s two types of output, stdout (standard out) for when a command is successful, and stderr (standard error) 
# for when it’s not. Both of these will print to the terminal by default. 
# You can redirect stderr with the 2> operator.
bad_command 2> stderr.txt
# You can redirect stdout with the 1> operator.
echo hello bash 1> stdout.txt

# <
# stdin (standard in) is the third thing commands can use and 
# is for getting input. The default is the keyboard
# You can redirect input to file.
read NAME < name.txt

# |
# Redirection output from previous command to input to next.
# It worked, but it doesn't look like it. When you used the pipe (|) to set the input for read, it ran the command in a subshell or subprocess. 
# Basically, another terminal instance within the one you see. The variable was set in there and didn't affect the one you had previously set. 
echo TOM | read NAME



# CAT
# cat get input and print it.
# read the contents from terminal and print it.
cat
# read content from file - file is an argument
cat file.txt
# read content from stdin redirected to file
cat < file.txt  -  does the same but with redirection stdin
# print Alex with redirection stdin with |.
echo "Alex" | cat
# You can create file with cat. Run command
cat > newfile.txt # then enter text and press CTRL + D
# Also you can append some text to file
cat >> file.txt # then enter text and press CTRL + D



#REDIRECTION SAMPLES
# script.sh:
#!/bin/bash
read NAME
echo "Hello $NAME"
bad_command

echo "Alex" | ./script.sh # redirect output of echo to script.sh
echo "Alex" | ./script.sh > stdout.txt 2> stderr.txt # redirect output from echo to script.sh. Redirect stdout and stderr to stdout.txt and stderr.txt
./script.sh < name.txt > stdout.txt 2> stderr.txt # redirect stdin to name.txt, stdout to stdout.txt and stderr to stderr.txt



# TEE 
# tee - read from standard input and write to standard output and files
cat text.txt | tee text1.txt # print standard input to terminal and overwrites text1.txt
# Use -a flat append text to file
cat text.txt | tee -a text1.txt
# Note, you can work with output of tee 
cat text.txt | tee -a text1.txt | cat


# WC
# shows how many lines, words and bytes in the file.
wc kitty_ipsum_1.txt # 27  332 1744 kitty_ipsum_1.txt
# shows how may lines in the file.
wc -l kitty_ipsum_1.txt
# shows how may word in the file.
wc -w kitty_ipsum_1.txt
# shows how may character in the file.
wc -m kitty_ipsum_1.txt

cat kitty_ipsum_1.txt | wc # redirect contents of kitty_ipsum_1.txt to wc.
wc < kitty_ipsum_1.txt # redirection stdin.
cat kitty_ipsum_1.txt | wc -l >> kitty_info.txt # append number of lines in kitty_ipsum_1.txt to kitty_info.txt



# GREP
# finds lines in file by pattern and prints this lines

# finds lines with meow in kitty_ipsum_1.txt
grep "meow" kitty_ipsum_1.txt
# finds lines with meow and highlights this word.
grep --color "meow" kitty_ipsum_1.txt
# prints a line number for every printed line.
grep -n "meow" kitty_ipsum_1.txt
# you can use regular expressions.
grep "meow[a-z]*" kitty_ipsum_1.txt
# prints lines count which contains word "meow"
grep -c "meow" kitty_ipsum_1.txt 
# puts the matches on their own lines.
grep -o "meow[a-z]*" kitty_ipsum_1.txt 
# You should add -E - extended regex flag, to use some features of regex.
# Search several patterns
grep --color -E 'cat[a-z]*|meow[a-z]*'
# To search recursively in all files in a folder use -r.
grep -r "some text" . # search "some text" in all files in current directory
# and subfolders.
# With the -v flag you can show all lines that don't contain pattern
grep -v "printed" sample.txt 
# To find exact word use -w flags
grep -w exam text.txt
# You can print lines before or after matching line with 
# -Acount (after) and -B count (before)
grep -A1 -B2 third text.txt # additionally pring one line before and 2 line after
# You can use any positive numbers with -A and -B.







#SED
# sed replaces some text in file
# replace free with f233 in file name.txt. It do not modify raw file, just prints result to the output.
sed "s/free/f233/" name.txt 
# ignore case of free
sed "s/free/f233/i" name.txt 
# you can use regex with -E flag. Also you can use regex groups in replace part /\1/
grep -n "meow[a-z]*" kitty_ipsum_1.txt | sed -E "s/([0-9]+).*/\1/"
# you can replace several words
sed "s/catnip/dogchow/; s/cat/dog/"
# add flag g to replace all enterances of word (by default on the first will be replaced)
sed "s/catnip/dogchow/g; s/cat/dog/g"



#DIFF
diff kitty_ipsum_1.txt doggy_ipsum_1.txt # show diffs between two files.
diff --color kitty_ipsum_1.txt doggy_ipsum_1.txt # show diffs with color.


# You can use $1 in script to retrieve first argument or if there is no input stdin.


#DU
# It summarizes disk usage of the set of FILEs, recursively for directories.
# In prints disk usage for every file and folder in a specified directory
du . # for current directory
# You can specify several file / folders
du test.txt get-docker.sh
# You can print only total result for each argument with the flag -s(summarize)
du -s .
# You can add flags -k, -m to show results in kb, mb.
du -sk .
# For one file
du -sk test.img # -k size in MG
du -sh test.img # -h human readable size



# TAR
# tar - tape archive
# Used to archive data
# Files created with tar often called tarballs.
# Create an archive
tar -cf test.tar file1 file2 file3 # -c - create archive, 
                                   # -f - specify archive name first
# Show contents of the archive
tar -tf test.tar # You should specify -f if specifing file name, -t - show contents
# Extract archive
tar -xf test.tar # -x - extract
# By default created archive isn't compressed, to compress use -z option
tar -czf test.tar file1 file2 file3
# Use -C to extract to a specified folder
tar -xf caleston-code.tar -C /opt/ 
# Unzip .gz archive
tar -C /opt/ -xvf caleston-code.tar.gz




# BZIP2, GZIP2, XZ, BUNZIP2, GUNZIP, UNXZ, ZCAT, BZCAT, XZCAT
# There are some commands specifically used for compression
# They don't save origin file
bzip2 test.img # Result: test.img.bz2
bunzip2 test.img.bz2 # Uncompress
gzip test1.img # Result: test1.img.gzip
gunzip test1.img.gzip # Uncompress
xz test2.img # Result: test2.img.xz
unxz test2.img.xz # Uncomporess

# You can read compressed files with
# zcat, bzcat, xzcat
bzcat test.txt.bz2 # result - file content



# LOCATE, UPDATEDB
# To find a file use command locate.  It prints all paths with keyword in it.
locate test2.txt # /home/alex147/test2.txt
# Note, it depends on database called mlocate.db for querying the file name.
# If the file you are trying to locate wa created recently, this command
# may not yield useful results. It is possible that the db has not been updated yet.
# To update db use the command:
updatedb



# FIND
# Also to search file you can use find command:
find dir -name filename # search in dir filename
find . -name test2.txt # search in current directory file with name test2.txt
./test2.txt



# DF
# Show information about the file system on which each FILE resides,
# or all file systems by default.
# Show file systems memory usage
df
# Show file systems type:
df -T










