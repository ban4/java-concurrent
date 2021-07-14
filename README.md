# 并发编程
3.3 查看进程线程的方法 
windows
任务管理器可以查看进程和线程数，也可以用来杀死进程
tasklist 查看进程 taskkill 杀死进程

linux
ps -fe 查看所有进程
ps -fT -p <PID> 查看某个进程(PID)的所有线程 kill 杀死进程
top 按大写 H 切换是否显示线程
top -H -p <PID> 查看某个进程(PID)的所有线程

Java
jconsole 远程监控配置 需要以如下方式运行你的 java 类
修改 /etc/hosts 文件将 127.0.0.1 映射至主机名 如果要认证访问，还需要做如下步骤
复制 jmxremote.password 文件
修改 jmxremote.password 和 jmxremote.access 文件的权限为 600 即文件所有者可读写 连接时填入 controlRole(用户名)，R&D(密码)
jps 命令查看所有 Java 进程
jstack <PID> 查看某个 Java 进程(PID)的所有线程状态 jconsole 来查看某个 Java 进程中线程的运行情况(图形界面)
# java-concurrent
