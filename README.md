# anylog

## 关于anylog
		QQ群：287863133
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
		5、打印方法耗时，支持方法嵌套。

		如果需要扩展新的功能（例如输出jvm的cpu占用，内存大小等），只需要实现spi中的
		com.github.jobop.anylog.spi.TransformDescriptor 
		和com.github.jobop.anylog.spi.TransformHandler接口，
		然后把实现的jar包放到providers目录中即可识别。


## 使用方法
		1、获取运行程序：
			1）可以到以下地址获取正式发行版：https://github.com/jobop/release/tree/master/anylog
			2）你也可以clone下源码后，执行如下命令，生成运行程序，生成的运行程序将在dist目录下
			    生成windows版本:  mvn install
			    生成linux版本:  mvn install -Plinux
		2、直接执行startup.bat或者startup.sh即可运行起来
		3、访问 http://127.0.0.1:52808 即可使用 
		
## 功能扩展
		anylog利用spi机制实现其扩展，如果你想要对anylog增加新的功能（例如添加返回值打印的功能）可以按照如下步骤操作：
		1、使用如下命令，生成一个spi实现工程，并导入eclipse
		mvn archetype:generate -DarchetypeGroupId=com.github.jobop -DarchetypeArtifactId=anylogspi-archetype -DarchetypeVersion=1.0.4
		2、参照该工程中已有的两个例子（一个是在方法开始插入日志，一个是在方法结束插入日志），实现TransformDescriptor和TransformHandler接口
		3、把两个接口实现类的全路径，分别加到以下两个文件中
		    src/main/resources/META-INF/services/com.github.jobop.anylog.spi.TransformDescriptor
		    src/main/resources/META-INF/services/com.github.jobop.anylog.spi.TransformHandler
		4、执行mvn install打包，在dist下会生成你的扩展实现jar。
		5、把扩展实现jar拷贝到anylog的providers目录下，重启即可生效。
		
		tips：在实现spi时，我们提供了SpiDesc注解，该注解作用在你实现的TransformDescriptor上，可以用来生成功能描述文字。
		     如果要深入了解spi机制，请自行google：java spi

## 使用技术栈
		attachApi，Instrumentation ，javassist，spi，classloader机制，socket
			     
## 常见问题
		1、启动后看不到vm列表怎么办？
		首先检查所使用的版本是否正确，如果版本正确，有可能是权限的问题，权限问题往往在win7出现较多，可尝试使用管理员权限启动。
		2、使用eclipse启动tomcat无法生效。
		使用eclipse启动tomcat之后，tomcat的进程是独立于eclipse的，有的同学会误注入了eclipse的进程，从而看不到效果。一般来说，启动tomcat后，anylog会在30秒内检测出tomcat进程。
		3、如何撤销修改？
		当jvm被修改后，在jvm列表界面，此进程后面会出现红色的restore_vm链接，点击即可恢复。当anylog退出时也会自动恢复。

		感谢 @aqxiebin @tebaton 提供支持