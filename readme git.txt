create a new repository on the command line

echo "# math-lib" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/dwbzen/math-lib.git
git push -u origin master
