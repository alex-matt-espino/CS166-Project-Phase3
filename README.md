# Fall CS166 Final Project Template

*By Connor Hardenburgh*

Template for the final project of CS166, Fall 2022 at UCR.

Contains unmodified code from the given file `project.zip`, as well as the pdf file containing the project's instructions.

This file, `README.md`, contains many useful instructions for how to use ssh and VS Code, to make working on this project much easier.

Click `Use this template` and create your own (private) repository to use it. 

**I HIGHLY SUGGEST YOU NAME THE REPO `project` AS THAT WILL MAKE SURE YOUR FOLDER NAME IS ALWAYS `project`**

## Use Instructions

### How to access a lab machine to work on the project using VS Code:

Add the [Remote - SSH](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-ssh) extension to VS Code if it isn't already installed.

You should have an [OpenSSH compatible SSH client](https://code.visualstudio.com/docs/remote/troubleshooting#_installing-a-supported-ssh-client) already installed, but if not, install one.

Press `F1` and press enter on `Remote-SSH: Add New SSH Host`. Enter into the box `ssh -oProxyJump=[username]@bolt.cs.ucr.edu [username]@[lab_computer]`. Now, press `F1` again and press enter on `Remote-SSH: Connect to Host`. Press enter on the lab machine, and type your password into the box. You might have to type your password twice, once for `bolt.cs.ucr.edu` and again for the lab machine.

You are now in the lab machine! VS Code might ask what OS is being run, select Linux and wait for VS Code to install anything it needs to. ~~Now, you can open whatever folder you want to work in by clicking `File -> Open Folder...` or pressing `Ctrl-K, Ctrl-O`.~~ (This should be unnecessary as we are going to be cloning repos, but this is useful in all other situations.)

### How to work on the repo through a lab machine.

VS Code's GUI makes using git much simpler than through the terminal, which is great.

Either press `Ctrl-Shift-E` or click the icon with two sheets of paper at the top left of VS Code. If you do not have a folder currently opened, there should be a button which says `Clone Repository`. Click it. Enter the HTTPS link to your repo and press enter. Enter the directory you want this repo to be placed in, the default should be fine. 

VS Code may ask you to link it with your Github account at some point, do so. 

The repo should now be downloaded onto the lab machine and viewable in VS Code. You can make edits using VS Code and run commands within its integrated terminal, accessable by pressing `Terminal -> New Terminal` or `` Ctrl-Shift-` ``. 

You can make commit, pull, make branches, etc. using VS Code's `Source Control` tab, accessible by clicking the tree-like icon at the left of the application (underneath the magnifying glass) or by pressing `Ctrl-Shift-G`. You can also make branches by pressing the same icon (which says the name of the branch you are currently working on, probably `main`) at the bottom-left of the application. 

### Using SSH keys

> A safe way to simplify logging into remote machines
> 
> Also a safe way to log into Github, and other useful services

**ON YOUR LOCAL MACHINE**

#### Generate keys
```
ssh-keygen
```

#### Copy your public key onto the remote server

> Do this for `bolt.cs.ucr.edu`, but it will NOT work with your lab machine, as `~/.ssh` will be wiped as soon as you log out, making this step useless.
```
ssh-copy-id [username]@[server]
```

> `ssh-copy-id` does NOT work in native Windows, instead use this command in Powershell:
```
type $env:USERPROFILE\.ssh\id_rsa.pub | ssh [username]@[server] "cat >> .ssh/authorized_keys"
```

#### Using your public key with Github

> This will allow you to use git commands on the repo without needing to put your password in constantly.
> 
> If you have connected Github to VS Code, this shouldn't be necessary, but it's a nice-to-have.
> 
> Don't bother doing this in the lab machine, like said before, your keys located at `~/.ssh` will just be deleted.

While logged into Github, go to [Settings -> SSH and GPG keys](https://github.com/settings/keys).

Click `New SSH key` and copy the text inside the local file you created in the `Generate Keys` step. By default it is located at `~/.ssh/id_rsa.pub` on Mac and Linux, and `C:\Users\[username]\.ssh\id_rsa.pub` on Windows. If in a terminal, you can read the file's contents using `cat id_rsa.pub`. Place the copied text into the text box, name your key, and hit `Add SSH key`. 

## Dependencies, if not running on a lab machine

> ~~Differences in directory structure between the lab and normal machines should make this useless~~
> 
> ~~If a way to use relative filepaths with SQL is found, this will become relevant~~
>
>> I have done the testing, and this is totally possible. 

### For Ubuntu on WSL

> Should work for any distro, WSL or not, that uses `apt` as a package manager
> 
> Should be able to be easily edited for other distros/package managers

#### Update `apt`, upgrade packages (not always necessary, but good practice)
```
sudo apt update
sudo apt upgrade
```

#### Install `postgresql` and edit your path to allow for `postgresql` commands to work

```
sudo apt install postgresql
export PATH=/usr/lib/postgresql/[version, probably 12]/bin:$PATH
```

> It is also a good idea to edit your `.bashrc` so you don't need to update your path every time you start Linux, by editing `~/.bashrc` and adding these lines to the bottom:

```
# custom
export PATH="$PATH:/usr/lib/postgresql/12/bin"
```

#### Install JDK

> If not available, search an openjdk package that works on your distro and install that. `java/scripts/compile.sh` might need to be edited accordingly. JDK 8 should be used, as the given java code appears to have depricated functions and will not compile with newer versions (Tested with JDK 11).

```
sudo apt install openjdk-8-jdk
```


