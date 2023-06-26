# You can ran bash in bash. Just type bash. When you exit from current shell, you go to previous shell. 
# If it is the the most up shell the terminal will be closed.
# Script runs in his own shell.


# INITIALIZATION:
sh script.sh # run script with shell interpreter
bash script.sh # run script with bash interpreter
bash -x test.sh # debug your script.

which bash / sh # bash / sh location
which ls # ls location

# You should add 
#!/bin/bash
# at the top of the script to specify that it is a script for bash, not 
# for sh, dash, ...
# add executable right for all users. Check it with ls -l.
chmod +x script.sh 
# You can run it with
./script.sh



# DEBUGGING
# THERE IS A BUSH DEBUGGER FOR VSCODE AND IT WORKS!!!
# https://marketplace.visualstudio.com/items?itemName=rogalmic.bash-debug

# We have script test.sh:
#!/bin/bash
a=1
b=2
if [[ $a -lt $b ]]
then
    echo "A bigger"
else
    echo "B bigger"
fi

# Run script with -x option, that will output to terminal current command
# with values before it will be executed.
bash -x test.sh
+ a=1
+ b=2
+ [[ 1 -lt 2 ]]
+ echo 'A bigger'
A bigger

# Run script with -v to print lines be they will be executed. The difference
# with -x in constructions
bash -xv test.sh
a=1
+ a=1
b=2
+ b=2
if [[ $a -lt $b ]]
then
    echo "A bigger"
else
    echo "B bigger"
fi
+ [[ 1 -lt 2 ]]
+ echo 'A bigger'
A bigger

# Use set do enable debugging for part of the code in the script.
set -xv
if [[ $a -lt $b ]]
then
    echo "A bigger"
else
    echo "B bigger"
fi
set +xv

# You can use -n option to just check the syntax of the script, don't
# execute it
bash -n test.sh

# To recognize unset variable use -u option
# The -u option treats unset variables and parameters as 
# an error when performing parameter expansion.
bash -u test.sh

# use trap command to output some information before every command
#!/bin/bash
trap 'echo "Line:$LINENO a=$a b=$b"' DEBUG
a=1
b=2
if [[ $a -lt $b ]]
then
    echo "A bigger"
else
    echo "B bigger"
fi

echo "End"
# Output
Line:3 a= b=
Line:4 a=1 b=
Line:5 a=1 b=2
Line:7 a=1 b=2
A bigger
Line:12 a=1 b=2
End













# COMMENTS:
# Use sharp for comment.
# comment
# Multiline comment.
'
for(( i = $1; i >= 0; i--))
do
  echo $i
  sleep 1
done
'



# HELP:
help # show built-in commands
help built-in command name  -  learn more about a built-in command
/bin - location of non built-in command / utils
man command - for manual


  
# DIFFERENT:
sleep 5 # sleep 5 seconds.
printenv # print environment variables
echo $LANG # print env variable

# RANDOM - environment variable which returns random values between 0 and 32767.

Command type show a type of the command.
type read
read is a shell builtin
type if
if is a shell keyword
type bash
bash is /usr/bin/bash
type ./five.sh
./five.sh is ./five.sh



# VARIABLES:
# In bash variable is always string.
# Create variable (without spaces from left and right of equals). 
# Use "" for values with spaces.
QUESTION1="What's your name?"
# You also can declare variable with
declare QUESTION1="What's your name?" - this two declarations are equivalent.
# Show all varialbles. This list includes all the environment variables, 
# and any others that may have been created in the current shell.
declare -p # show all varibles.
declare -p J # show variable J.
# To get value utilize $variable_name
echo $QUESTION1

# Read variable from console
read NAME
# You can use -p to add a prompt for a user (some text that will be written)
read -p "Enter mission name:" mission_name
# Note, it better to user command line arguments to get data.
# In this case we don't need manual intervention. And, for example, run
# this script from another script.

# Get value
echo Hello $NAME.
# or
echo "Hello $NAME."
# or
echo "Hello ${Name}." # explicitly define variable name.

# You can pass the value of a variable into function
mission_name=mars-mission
mkdir $mission_name

# You can save command output into a variable, user $(command) for this
rocket_status=$(rocket-status $mission_name) # execute command rocket-status
# with mission_name value as argument
# Note, If the output goes over several lines then the newlines are simply removed 
# and all the output ends up on a single line.





# PARAMETERS:
# You can add argument to your script 
./countdown.sh arg1 arg2 arg3
echo $* # show all argument values.
echo $0 # command that started the script
echo $1 # first argument.
echo $? # exit status of last command.	
echo $# # arguments count.
# Note, it is recommended in the script assign every argument to a meaningful
# variable name

# You can separate commands on single line with ";"
# Every command always has exit status. If it equals 0 so there is no errors, 
# otherwise error occurs. 
# You can show exit status of last command this way.
ls; echo $?



# ARITHMETIC OPEERATIONS (( )), EXPR, BC COMMANDS
# (( arithmetic expression )) - the problem is that expression usually 
# returns non-zero value, which logically should be true.
# But bash uses status codes and 0 is true, and other number is false. 
# So (( )) carries out this convertion.
# ((  ))  - evaluates to 0 (shell true), if expression in (( )) not equal 0. 
#           evaluates to 1 (shell false), if expression in (( )) equal 1.
# Double parenthesis peform the calculation. 
# It support most arithmetic C operators.
# You can create a variable here, change other variables.
# All these changes will be visible outside the (( )).
# Note, you SHOULD NOT USE $ for variable value.
A=0
(( A+=10 )) # it not return any value, exit code 10 -> 0
# If you want to get a value use $
I=0
echo "$(( I + 4 ))"
# You can assign the calculated value to another variable.
J=$(( I - 6 ))
# (( )) can contain several operators. You should separate them with comma. 
# It will you return value of the last operator.
(( D = 12, F = 34 )) # 34 will be converted to 0.
# Note, (( )) SUPPORTS ONLY DECIMAL OUTPUT, IT CAN'T RETURN FLOATING NUMBER.
# You can embed the (( )) into a string
echo "Sum is $(( A + B ))"

# You can also use the expr command to calculate arithmetic operation
# But it only supports one operation. It prints value into standard output.
# Note, that the oparator and values should be stricly separated by 
# whitespace.
expr 3 + 5 # 8
# Save result into a variable
B=$( expr 3 + 6 )

# You can use the bc command to evaluate floating point operations
# Add -l flag and pass the input arithmetic string with pipe
echo "3 / 5 + 1" | bc -l



# CONDITIONS:
# For manual use:
# help [[
# help test

# [[  EXPRESSION ]] - return 0 or 1 status code, depending on the evaluation 
# of the conditional expression EXPRSSION.
# It's an operator [[ and some parameters after it. At the end goes ]].
# Spaces in brackets and before and after equals are important.
 
# In [[ ]] you can use:
# ( EXPRESSION )    Returns the value of EXPRESSION
# ! EXPRESSION      True if EXPRESSION is false; else false
# EXPR1 && EXPR2    True if both EXPR1 and EXPR2 are true; else false
# EXPR1 || EXPR2    True if either EXPR1 or EXPR2 is true; else false

# EXPRESSION is test ( [ ] is the same ) expression.

# In test you can use
# -lt, -le, -gt, -ge, -eq, -ne  - arithmetic operators

# <, >, == - string operators
# Use =~ operator for pattern matching.
# [[ "hello" =~ "el" ]] # true
# You can use regular expression, but you should not use quotes for them.
# [[ "hello world" =~ ^h ]] # starts with h

[[ $1 -lt 5 ]] # less then 5.
# And file commands
[[  -a test.txt ]]  -  if test.txt exists


# If exit status for if is 0 then executed. Otherwise else executed.
# In if you can use also test or [ ].
if [[ $1 == arg1 || $1 == arg2 ]]
if [ $1 == arg1 ] || [ $1 == arg2 ]
if test $1 == arg1 || test $1 == arg2
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


# CASE:
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



# LOOPS
# FOR
for (( i = 10; i > 0; i-- ))
do
  echo $i
done

# Range
for x in {0..100} # values from 0 to 100
do
done

# FOR IN
for x in a b c
do
    echo $x
done

# WHILE LOOP:
# You can use [[ ]] and (( )) for conditions.
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
  
# UNTIL LOOP:
# It will be executed will the condition is false.
I=0
until [[ I -gt 5 ]]
do
  echo "$I"
  (( I++ ))
done

# BREAK, CONTINUE
while true
do
  break
done
while true
do
  continue
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



# FUNCTIONS:
# Note, you should first define a function in your script before running it.
FUNCTION_NAME() {
  COMMANDS;
}
function printstr() {
	echo "hello world"
}
printstr #RUN

# Use $ to get arguments.
function printstr(){
	echo $1
}
printstr "Hello world"

# If we use the exit command in the function the whole script will exit.
# Not only the function.
# To return the exit status from the function use the return command:
if [ $status = "error" ]
then
	return 1
fi
# You can capture the exit code of the function the same way as for
# another commands
echo $?

# Note, if you create a variable in the function, it is available in
# outside scope
function read_numbers() {
    read -p "Enter Number1: " number1
    read -p "Enter Number2: " number2
}
read_numbers
echo Answer=$(( $number1 + $number2 )) # we can use here number1 and number2
# Also you can use variables from outside scope
#!/bin/bash
function print() {
    echo $a
    echo $b
}
a=1
b=2
print



# EXIT CODES
# Apart from output each command returns an exit code
# If exitcode = 0 => command is successfully executed
# If exitcode > 0 => there was an error
# To get exitcode of the last command use $?
echo $?
# It's a good practice to use exit codes in your script to indicate to the
# caller or the user of the script, the status of your script.
# If we don't specify any exit status, then the script will always
# automatically set the exit code to zero.
# We can specify exit code with exit command
if [ $mission_status = "failure" ]
then
    exit 1
fi

# What if we want to return some data from the function?
# You should echo this data and then capture the output with $( )
# test.sh
#!/bin/bash
function add() {
    echo $(( $1 + $2 ))
}
res=$( add $1 $2 )
echo $res
# Run
./test.sh 1 2



# RUN COMMANDS
# You can get command output with `command`. It return exit code if it has 
# no output else output.
for file in `ls`
do
   echo "$file"
done
# Or
for file in $( ls )

# You can get exit code for command with output like this:
if [[ $? -eq 0 ]]
then

fi










