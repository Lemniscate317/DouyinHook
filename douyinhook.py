import frida, sys,io

def on_message(message, data):
    if message['type'] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)

jscode = """
Java.perform(function () {
    var MainActivity = Java.use('com.ss.android.ugc.aweme.feed.share.a.b');
    var aweUse= Java.use("com.ss.android.ugc.aweme.feed.model.Aweme")

    // Whenever button is clicked
    MainActivity.a.overload('com.ss.android.ugc.aweme.feed.model.Aweme','java.lang.String').implementation = function (awe,string) {
        //var aweCast = Java.cast(awe,aweUse)
        //console.log(awe)
        var video = awe.getVideo();
        var playurlList = video.getPlayAddr().getUrlList();
        var downloadList = video.getDownloadAddr().getUrlList();
        downloadList.clear()
        downloadList.addAll(playurlList)
        console.log('------------------')
        console.log(playurlList)
        console.log('------------------')
        console.log(downloadList)

        this.a(awe,string)
    };
});
"""

process = frida.get_usb_device().attach('com.ss.android.ugc.aweme')
script = process.create_script(jscode)
script.on('message', on_message)
print('[*] Running CTF')
script.load()
sys.stdin.read()