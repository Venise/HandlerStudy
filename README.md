##Handler机制：
###1、Handler是什么？

* handler是Android给我们提供来更新UI的一套机制，也是一套消息处理的机制，
我们可以发送消息，也可以通过它来处理消息，handler在我们的framework中是非常常见的。
 * 例如：Activity生命周期 onCreate() onStart()生命周期的回调方法都是通过Handler发送消息，
并根据不同的Message做相应的分支处理。

 * ActivityThread AMS(Activity Manager Service) 事件传递
   
###2、Handler怎么用？
* 官网Hanler介绍【图片】
* 创建一个进程时就是一个main线程，运行message queue，管理顶层的activity，
broadcast，receivers等。自己创建的线程通过handler和主线程通信，调用post和sendMessage方法，
给定的runnable或者message会被放入handler的message queue并在适当时机执行。

####Handler方法：

* 利用handler发送消息
　*　一种实例化Message方法,创建一个Message： Message msg=new Message();
　*　一种调用handler.obtainMessage()复用系统的message对象： Message msg=handler.obtainMessage();
　*　sendMessage
　*  sendMessageDelayed
　*  post(Runnable)
　*  post(Runnable, long)
* 利用handler移除消息
  * handler.removeCallbacks(myRunnable);
* 可以指定callback
  * 创建handler时指定一个callback，重写handlerMessage（）法
  * 利用callback截获handler的消息，ture：截获消息
*　msg.sendtoTarget(); 也可以发送到Handler对对象进行处理,效果等同于 handler.sendMessage(msg);


###3、为什么使用Handler?
   Android在设计的时候，就封装了一套消息的创建、传递、处理机制，如果不遵循这样的机制，
就没办法更新UI信息，就会抛出异常信息。

###4、Android为什么要设计只能通过Handler机制更新UI？
* 最根本目的在与解决多线程并发问题。
* 假设在一个Activity中，有多个线程去更新UI，并且都没有加锁机制，会导致界面更新混乱，
如果加锁的话会导致性能下降，所以google提供了一套更新UI的机制，不用去关心多线程问题，
所有的UI更新操作都是在主线程的消息队列当中去轮询处理。

###5、Handler的原理是什么？
* handler封装了消息的发送，发送会有地址，一般是一个MessageTarget，通过handler发送消息，默认情况下发送给自己
* Looper轮询
 * 内部包含一个消息队列，所有的handler发送的消息会装入这个消息队列
 * Looper.Looper方法，是死循环，不断从MessageQueue取消息，如有消息就处理，没有就阻塞
* MessageQueue可以添加并处理消息
* handler内部跟Looper进行关联，在handler内部可以找到looper，也就找到了MessageQueue，在handler中发消息就是向消息队列发消息（关联方法写在了handler的构造方法中）
* 总结：handler负责消息发送，looper负责接收handler发送的消息，并直接把消息回传给handler自己，messagequeue是一个存储消息的容器

 * ActivityThread main Looper MessageQueue
 * Looper源代码
 * 图解原理：handler是操作的实际对象，发送及处理。looper只是传达消息

###6、使用Handler时候遇到的问题

###7、如何实现一个与线程相关的Handler？

class MyThread extends Thread {
	public Handler handler;
    	
		@Override
		public void run() {
			Looper.prepare();
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					System.out.println("currentThread: " + Thread.currentThread());
				}
			};
			Looper.loop();
		}
    }

###8、HandlerThread又是什么？
* 相当于某一类功能比较耗时的处理并且这些操作分布在各个线程中，又需要互斥处理，全部丢到HandlerThreadr的线程处理，
这可避免添加很多互斥变量。（直接使用普通Thread处理也类似）如果不耗时， 可以丢至主线程处理即可。
C++中，没有这种机制，一般通过加锁实现多线程的互斥。
* 不同线程间的同步
* 为空等待；唤醒
* HandlerThread创建looper，此looper与handler相关联，handler的handlerMessage在子线程中执行。
* 异步任务
* 开启线程；添加任务；传递派发；添加移除；发送处理；

###9、如何在主线程给子线程发送消息？



###10、Android中更新UI的几种方式
更新UI的4种方式：
* 通过Handler的post方法()；
* 调用Handler.sendMessage()方法；传统的方法
* 重写Activity中的runOnUIThread方法更新；
* 调用View自身的post(Runnable run)方法更新；


###11、非主线程真的不能更新UI吗？

* --->刚启动的时候，立即在非UI线程更新->不报错。
--->休眠2s钟以后，更新——————>报错
更新UI-->会调用checkForRelayout()方法
-->invalidate()方法-->invalidate(true)方法，关注viewParent-->ViewRootImpl是ViewParent的实现类
--->p.invalidateChild()-->查看ViewRootImpl.invalidateChild()-->checkThread()方法-->判断UI线程是否是当前线程，不相等抛出异常。

* ViewRootImpl是onResume()方法才会创建。所以onCreate()方法中要延迟才可以。

* handleResumeActivity()方法---》viewManager.addView()-->ViewRootImpl初始化。关注viewParent-->ViewRootImpl是ViewParent的实现类
