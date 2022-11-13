cd ../.. # go two levels up

ls -l # long list format
clear # clear terminal

echo hello website  #  print hello website in terminal
echo I made this boilerplate >> README.md # write something into file

touch index.html # create index.html
touch client/assets/fonts/roboto-bold.woff # create roboto-bold.woff in folder client/assets/fonts

ls --help # help for ls 
man ls # manual for ls
ls -a # (--all) show hidden files too

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
rmdir images # remove empty directory

more filename # view content of a file