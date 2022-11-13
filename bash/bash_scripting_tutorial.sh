В терминале работает программа оболочка, которая интерпритирует команды, производит необходимые действия. Это sh, bash 
You can ran bash in bash. Just type bash. When you exit from current shell, you go to previous shell. 
If it is the the most up shell the terminal will be closed.
Script runs in his own shell.


INITIALIZATION:
sh script.sh  -  run script with shell interpreter
bash script.sh  -  run script with bash interpreter
bash -x test.sh  -  debug your script.

which bash / sh - bash / sh location

You should add 
#!/bin/bash
at the top of the script to not specify bash / sh when run the script
chmod +x script.sh  -  add executable right for all users. Check it with ls -l.
You can run it with
./script.sh



COMMENTS:
Use # for comment.
# comment
Multiline comment.
: '
  for(( i = $1; i >= 0; i--))
  do
    echo $i
    sleep 1
  done
  '



HELP:
help - show built-in commands
help built-in command name  -  learn more about a built-in command
/bin - location of non built-in command / utils
man command - for manual


  
DIFFERENT:
sleep 5 - sleep 5 seconds.
 
printenv  -  print environment variables
echo $LANG  -  print env variable

RANDOM - environment variable which returns random values between 0 and 32767.

Command type show a type of the command.
type read
read is a shell builtin
type if
if is a shell keyword
type bash
bash is /usr/bin/bash
type ./five.sh
./five.sh is ./five.sh



VARIABLES:
In bash variable is always string.
Create variable (without spaces from left and right of equals). Use "" for values with spaces.
QUESTION1="What's your name?"
You also can declare variable with
declare QUESTION1="What's your name?" - this two declarations are equivalent.

Show all varialbles. This list includes all the environment variables, and any others that may have been created in the current shell.
declare -p  -  show all varibles.
declare -p J  -  show variable J.

For use variable utilize $variable_name
echo $QUESTION1

Read variable from console
read NAME

Write variable
echo Hello $NAME.
or
echo "Hello $NAME."
or
echo "Hello ${Name}." - explicitly define variable name.






PARAMETERS:
You can add argument to your script 
./countdown.sh arg1 arg2 arg3
echo $*  -  show all argument values.
echo $1  -  first argument.
echo $? - exit status of last command.	
echo $# - arguments count.
echo $0 - script path

You can separate commands on single line with ";"
Every command always has exit status. If it equals 0 so there is no errors, otherwise error occurs. 
You can show exit status of last command this way.
ls; echo $?


CONDITIONS:
For manual use:
help [[
help test

[[  EXPRESSION ]]  - return 0 or 1, depending on the evaluation of the conditional expression EXPRSSION.
Its operator  [[ and some parameters after it. At the end goes ]].
Spaces in brackets and before and after equals are important.
 
In [[ ]] you can use:
( EXPRESSION )    Returns the value of EXPRESSION
! EXPRESSION              True if EXPRESSION is false; else false
EXPR1 && EXPR2    True if both EXPR1 and EXPR2 are true; else false
EXPR1 || EXPR2    True if either EXPR1 or EXPR2 is true; else false

EXPRESSION is test ( [ ] is the same ) expression.

In test you can use
-lt, -le, -gt, -ge, -eq, -ne  - arithmetic operators

<, >, == - string operators
Use =~ operator for pattern matching.
[[ "hello" =~ "el" ]] # true
You can use regular expression, but you should not use quotes for them.
[[ "hello world" =~ ^h ]] #starts with h

[[ $1 -lt 5 ]]  -  less then 5.
And file commands
[[  -a test.txt ]]  -  if test.txt exists


If exit status for if is 0 then executed. Otherwise else executed.
In if you can use also test or [ ].
if [[ $1 == arg1 || $1 == arg2 ]]
# if [ $1 == arg1 ] || [ $1 == arg2 ]
# if test $1 == arg1 || test $1 == arg2
then
  echo true
else
  echo false
fi

if condition
then

elif condition

else

fi


CASE:

case $1 in                       #value
    "a")
        echo "It's a"
        ;;
    "b")
        echo "It's b"
        ;;
    *)                           #default
        echo "Something else"
esac


(( )) OPERATOR
(( arithmetic expression )) - the problem is that expression usually return non-zero value, which logically should be true.
But bash uses status codes and 0 is true, and other number is false. So (( )) carries out this convertion.
((  ))  - evaluates to 0 (shell true), if expression in (( )) not equal 0. 
          evaluates to 1 (shell false), if expression in (( )) equal 1.
Double parenthesis peform the calculation. It support most arithmetic C operators.
Note, you should not use $ for variable value.
A=0
(( A+=10 )) - it not return any value, exit code 10 -> 0
If you want to get a value use $
I=0
echo "$(( I + 4 ))"
You can assign the calculated value to another variable.
J=$(( I - 6 ))
(( )) can contain several operators. You should separate them with comma. It will you return value of the last operator.
(( D = 12, F = 34 )) #34 will be converted to 0.

FOR LOOP:
for (( i = 10; i > 0; i-- ))
do
  echo $i
done
FOR LOOP:
for x in a b c
do
    echo $x
done


WHILE LOOP:
You can use [[ ]] and (( )) for conditions.
while [[ $I -ge 0 ]]
  do
    echo $I
    (( I-- ))
    sleep 1
  done
while (( I >= 0 ))
  do
    echo "$I"
    (( I-- ))
    sleep 1
  done
  


UNTIL LOOP:
It will be executed will the condition is false.

I=0
until [[ I -gt 5 ]]
do
  echo "$I"
  (( I++ ))
done



ARRAY

ARR=("A" "B" "C") - create indexed array
ARRAY=([0]="a" [1]="B", …) - create array with defining indexes.
ARR[0]="D" - set value
echo ${ARR[0]} - get zero index value
${ARR[@]}, ${ARR[*]} - get all values

You can use $ many time to get array item using index variable
N=$(( RANDOM % 6 ))
echo ${RESPONSES[$N]}



FUNCTIONS:

FUNCTION_NAME() {
  COMMANDS;
}
printstr(){
	echo "hello world"
}
printstr #RUN

Use $ for get arguments.
printstr(){
	echo $1
}
printstr "Hello world"



RUN COMMANDS

You can get command output with `command`. It return exit code if it has no output else output.
for file in `ls`
do
   echo "$file"
done
You can get exit code for command with output like this:
if [[ $? -eq 0 ]]
then

fi







