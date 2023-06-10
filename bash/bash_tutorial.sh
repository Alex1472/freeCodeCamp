# USE TAB TO COMPLETE COMMAND, ARGUMENT, OPTION, ...

# Note, by default, in all commands you specify relative path cd myfolder
# Also you can speficy absolute path /home/alex/myfolder



cd ../.. # go two levels up
cd # (without argument) go to the home directory

ls -l # long list format
ls -a # show hidden files
clear # clear terminal

echo hello website  #  print hello website in terminal
echo I made this boilerplate >> README.md # write something into file

touch index.html # create index.html
touch client/assets/fonts/roboto-bold.woff # create roboto-bold.woff in folder client/assets/fonts

ls --help # help for ls 
man ls # manual for ls
ls -a # (--all) show hidden files too
ls -lt # show files in order of last modification
ls -ltr # show files in reverse order of last modification

cp background.jpg images # copy backgroud.jpg to images
cp -r website-boilerplate toms-website # copy website-boilerplate recursively into toms-website

rm background.jpg # remove backgroud.jpg
rm images/background.jpg # you can use complex path
rm -r fonts # remove folder and its contents

mv roboto.font roboto.woff # rename roboto.font to roboto.woff (file or folder)
mv roboto.woff fonts # move roboto.woff into folder fonts
mv index.html client/src # move index.html into client/src
mv header.png .. # move header.png to previous folder
mv images/footer.jpeg client/assets/images # move from one folder to another.

find # view file tree of the current folder
find client # view directory client tree
find -name index.html # find location of the index.html file or directory
find -name src # you can search a folder too

mkdir client/src # make an src directory in the client directory
mkdir Africa Europe Asia America # you can specify several folders to create
mkdir -p India/Mumbai # create chain of folders(India and Mumbai), -p == --parents
rmdir images # remove empty directory

more filename # view content of a file

pushd / # -  push on the top of the stack current directory goes to the 
# specified directory, / in this case
popd # takes from the stack last pushed direcotry(with the push command)
# and goes there
# Note, you can change directory with the cd command as many times as you want
# it doesn't influence on pushd/popd stack

whatis echo #displays one line description of command
man echo # show manual for the command, most commands come with bundled with 
# manual pages. It provides information about command in detail.
echo --help # several commands provides --help option to provide user
# with options and use cases.

alias dt=date # create alias dt that runs date command. Then you can run 
# just dt instead of data

history # show all entered commands

# To check the command location use which command
# If command won't print the location, that means the command wasn't found.
which nano