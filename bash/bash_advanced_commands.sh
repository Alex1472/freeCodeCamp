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



#REDIRECTION SAMPLES
# script.sh:
#!/bin/bash
read NAME
echo "Hello $NAME"
bad_command

echo "Alex" | ./script.sh # redirect output of echo to script.sh
echo "Alex" | ./script.sh > stdout.txt 2> stderr.txt # redirect output from echo to script.sh. Redirect stdout and stderr to stdout.txt and stderr.txt
./script.sh < name.txt > stdout.txt 2> stderr.txt # redirect stdin to name.txt, stdout to stdout.txt and stderr to stderr.txt



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



diff kitty_ipsum_1.txt doggy_ipsum_1.txt # show diffs between two files.
diff --color kitty_ipsum_1.txt doggy_ipsum_1.txt # show diffs with color.


# You can use $1 in script to retrieve first argument or if there is no input stdin.
