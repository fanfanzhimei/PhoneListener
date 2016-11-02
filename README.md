# PhoneListener
电话拦截器（在Activity中，接听电话时录制接听人的话筒声音）
demo中原是采用的模拟器+BroadcastReceiver的开机启动广播开启服务，但Android 4.0后，谷歌也意识到这个问题，很多手机厂商对此进行了屏蔽拦截，
导致程序收不到开机启动广播。
谷歌认为必须要用户手动点击了该应用后，我们才可以进行监听。因此必须要有Activity。
