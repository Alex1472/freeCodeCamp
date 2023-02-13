# You can create different invironments
# Envoronment consists of python and libraries
# So, if you have several environmants you have several python version
# and different libraries in every environment

pip list # list of libraries in a current environment
pip install virtualenv # install app to create environmants

# To create virtual environment run virtualenv env_name
# It creates folder in a current directory that stores python, pip and all libraries of this environmant.
virtualenv project


# Now in the current folder there is a folder project for this virtual environment
# Go to env_folder/Scripts
cd project/Scripts
# And run activate to use this environment
source ./activate
# To deactivate run deactivate in the Scripts folder
deactivate

# We can ask what pip we using now
where pip # first should be the pip of the current environment
D:\DifferentReps\PythonProjects\TestProjects\TestEnvPyProject\project\Scripts\pip.exe
C:\Users\tula1\AppData\Local\Programs\Python\Python310\Scripts\pip.exe


# pip freeze shows you all packages you installed
pip freeze
numpy==1.24.1
Pillow==9.4.0

# Usually you save it into a file, so that people use your project
# can install this packages into their environments
pip freeze > requirements.txt

# To install this requirements use:
pip install -r requirements.txt



# PIP
# Python package manager
# It install packages into you invironment
# To be clear every environment has its own pip
# pip install
# pip uninstall
# pip freeze
# pip list
# pip help - show help
# pip help command - help for a command

# show outdated libraries
pip list -o

# to upgrade package
pip install -U package_name
pip



# ANACONDA
# ANACONDA is a package manager and enviromant manager.
# Install and add to path it.
# run source ~/anaconda3/etc/profile.d/conda.sh

# Anoconda has a base environment 
# So when you install it, you will have at least 2 environments: global and base (from conda)
# base environment has many installed packages

# Show all conda environments
conda info --envs
# Activate envorinment
source activate env_name
conda activate env_name
# Deactivate
conda deactivate env_name
# Create
conda create --name env_name
# Create env and specify version
conda create --name old_python python=3.5 # python --version (check)

# Note, in VSCode you should specify environment in ide and environment in terminal differently.
# For ide, specify environment in a blue bar below terminal.
# For terminal, specify environment with conda activate, conda deactivate
# Alse, to run script USE ALWAYS PYTHON NOT PY, py always use global environment


# Check it there is a package in the repository
conda search package_name
# Install
conda install package_name
# Show all packages
conda list


# ALSO THERE IS A ANACONDA NAVIGATOR - GUI FOR PACKAGE MANAGER
   

