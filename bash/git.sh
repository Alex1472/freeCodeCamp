# GIT INIT
# Use git init to initialize repository in a current folder
git init


# GIT STATUS
# Use git status to see status of files your repository
git status


# GIT SHOW
# Show last commit details
git show
# Show 1 commit above in a tree
git show HEAD~1


#GIT CHECKOUT
# go to existing branch
git checkout branch_name
# You can create a new branch and switch to it
git checkout -b new_branch_name


#GIT ADD
# Use git add file_name to stage a file
git add README.md
# Use . to add all files
git add .


# GIT COMMIT
# To commit changes use git commmit -m "Commit name". -m stands for message
git commit -m "Initial commit"


# GIT LOG
# You can view commit history with git log
git log
# Show history, show commit in one line
git log --oneline
# Show information of last n commits:
git log -1
git log -5 --oneline


# GIT DIFF
# See changes in a repo.
git diff #It shows changes only for unstaged files

 
# GIT BRANCH
# Show all branches in your repository
git branch
# Create new branch (and do not checkout)
git branch new_branch_name
# Delete branch
git branch -d branch_to_delete
# Rename branch 
git branch -m old_branch_name new_branch_name


# You can use intereactive mode for rebase
git rebase HEAD~2 --intereactive
# The in nano you can specify how any commit will be rebased.
# You should change command before commit, default is pick
# pick - save commit as is
# drop - remove commit
# reword - change commit name, use changes as is
# squash - append to previous commit

#You can rename any commit with command 
git rebase --root --interactive # --root is a first commit in your repo. Then pick the commit and write reword, then enter new name.

# You can union several commits with squash. Write squash before commits you want to append, then enter the name of result commit.


# GIT MERGE
# Merge branch into current branch
# It create additional commit that is a child for two branches
git merge branch_name_to_merge


# GIT REBASE
# Rebase moves branch commits as children of specified branch
git rebase main # main is a branch where should rebase current branch


# FIX REBASE/MERGE CONFLICT
# If you have a merge conflict 
# You should fix it in a file (git add special characters >>>, <<<, ====)
# You should remove it and select right version
# Then stage file
# And run
git rebase --continue # for rebase


# GIT STASH
# You can stash your changes
git stash
# View all stashes
git stash list
# Apply last stash, and remove it from stashes
git stash pop
# Just apply last stash, do not delete it from stashes
git stash apply
# Note you can apply stash on another branch

# You can show changes in stash
git stash show # It show what files was changed and how many lines.
# You can specify stash to show with name from stash list (stash@{0} is latest)
git stash show stash@{1}
# You can show changes in files with -p (patch) flag
git stash show -p
# You can specify commit to show
git stash show stash@{1} -p
# You can remove stash
git stash drop # remove last stash
git stash drop stash@{1} # remove selected stash


# GIT RESET
# You can move branch pointer to another commit up in a tree
git reset HEAD~1 # move pointer on 1 commit up in a tree
# There is three modes for reset: --soft, --mixed, --hard
# --soft - all changes up the reset commit will be saved as staged files
# --mixed - all changes up the reset commit will be saved as unstaged files
# --hard - all changes up the reset commit won't be saved


# GIT REVERT
# You can create new commit to undo another commit
# To revert last commit
git revert HEAD
 

# README.md - is a file in a repository to describe what repo is for


# .GITIGNORE
# .gitignore file contains list of file that git should not track
# Example:
# .env


ls -a # show hidden directories 