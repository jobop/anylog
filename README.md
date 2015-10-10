# anylog

## 关于anylog
anylog为开发人员提供一个易于使用的平台，帮助开发人员在正在运行的系统中随时加入自己想要的日志，而免于修改代码和重启。



## 使用场景举例
		1、一些同学在写代码时，把异常吃掉了，使得问题难以查找，可以使用这个工具，动态打印出被吃掉
		    的异常，而不用停机。
		2、一些项目依赖第三方jar包，如果发生问题，但第三方包中无日志打印，以往可能需要重新编译第
		    三方包，加上日志，重启服务，然后排查问题。但使用这个工具，就可以直接动态加入日志，而不用
		    修改第三方jar包，也不用重启。

## 已有功能
		1、让系统打印某个exception的堆栈，无论此exception是否已经被吃掉都可打印
		2、在某个指定类的某个方法的某一行，输出日志。
		3、在某个指定类的某个方法的开始，输出日志。
		4、在某个指定类的某个方法的结束，输出日志。  

		如果需要扩展新的功能（例如输出jvm的cpu占用，内存大小等），只需要实现spi中的
		com.github.jobop.anylog.spi.TransformDescriptor 
		和com.github.jobop.anylog.spi.TransformHandler接口，
		然后把实现的jar包放到providers目录中即可识别。


## 使用方法
		1、获取运行程序：
			1）可以到以下地址获取正式发行版：https://github.com/jobop/release/tree/master/anylog
			2）你也可以clone下源码后，执行如下命令，构建运行程序，构建后程序将在dist目录下
			    windows:  mvn install
			    linux:  mvn install -Plinux
		2、直接执行startup.bat或者startup.sh即可运行起来
		3、访问 http://127.0.0.1:52808 即可使用 
