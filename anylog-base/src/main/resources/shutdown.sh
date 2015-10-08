ss=`more pid`
echo killed $ss ....
kill -3  $ss && kill $ss && sleep 3
ps -ef | grep 'anylog' | grep -v grep| awk '{print $2}' |xargs kill -9