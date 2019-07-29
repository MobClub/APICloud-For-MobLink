# 概述

移动端场景还原解决方案。10分钟快速集成MobLink，场景还原解决方案即可打破App孤岛，实现Web与App的无缝链接，让App间无缝跳转，加强用户体验，提升App活跃度。

## 配置集成

iOS平台设置参考 [iOS集成文档](http://wiki.mob.com/moblink-ios-doc/) 参考其中的第一点： **进行官网后台配置**

## 模块使用攻略

ios 需要将 plist 文件放入res目录下，文件内容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>MOBAppKey</key>
    <string>moba6b6c6d6</string>
    <key>MOBAppSecret</key>
    <string>b89d2427a3bc7ad1aea1e1e8c1d36bf3</string>
    <key>CFBundleURLTypes</key>
    <array>
        <dict>
            <key>CFBundleTypeRole</key>
            <string>Editor</string>
            <key>CFBundleURLSchemes</key>
            <array>
                <string>在Mob官网后台配置的URL Scheme</string>
            </array>
        </dict>
    </array>
</dict>
</plist>
```

Android 只需要配置config.xml，添加一下内容：

```
<feature name="mobSDK">
        <param name="Mob-AppKey" value="你的AppKey"/>
        <param name="Mob-AppSecret" value="你的AppSecret"/>
 </feature>
```

字段描述:
* Mob-AppKey：（必须配置）从Mob官网获取的 AppKey。AppKey 申请方法参考快速集成获取apppkey和appSecret。

* Mob-AppSecret：（必须配置）从Mob官网获取的 AppSecret。AppSecret 申请方法参考快速集成获取apppkey和appSecret。

> Android模块接入特别注意:
> 由于apicloud只能配置urlshceme 到 entrancActivity，这导致部分场景下无法进行还原，所以请在mob官网moblink 配置andriod的scheme为 mlink。

### widget\res\UZApp.entitlements的配置

该文件是给iOS平台配置的文件，在widget\res下创建文件名为UZApp.entitlements的文件，UZApp.entitlements内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>com.apple.developer.associated-domains</key>
    <array>
    <!--这里换成你在mob后台获取到的iOS的“Universal Link”值-->
        <string>applinks:z.t4m.cn</string>
    </array>
</dict>
</plist>
```

## 模块接口

### （1）getMobId（获取Mobid）

```
getMobId({params}, callback(ret, err))
```
params
* path:
类型：json字符串（只能包含path和params，请参考示例代码）
描述：在Mob后台配置的需要还原的控制器对应的路径。

* params:
类型：json字符串
描述：此时传入的字典数据，在场景还原时能够重新得到。

callback(ret, err)

* ret：
类型：JSON 对象
内部字段：
{
    mobid : 'b2E7Jr',  //此处是示例id
    domain: 'https://z.t4m.cn/'  //示例
}

* err：
类型：JSON 对象
内部字段：
{
    error : {'code' : code, 'userInfo' : error.userInfo}
}

#### 示例代码：

```js
var moblink = api.require('moblinkpro');
        moblink.getMobId({
            path:"/apicloud/demo",
            params:{"key1":"value1","key2":"value2"}
        },function(ret, err){
            if(err){
                alert(JSON.stringify(err))
                return
            }
            if(ret){
                alert("请在您网页的a标签中绑定\nherf=\""+ret.domain+ret.mobid+"\"" + "\n点击后即可打开您的App")
                api.toast({
                    msg: JSON.stringify(ret),
                    location: 'middle'
                });
            }
        });
```

#### 可用性

iOS系统，Android系统

可提供的1.0.3及更高版本

### （2）restoreScene（获取场景数据 请在apiready() 函数中进行调用）

```
restoreScene(callback(ret))
```

callback(ret)
* ret：
类型：JSON 对象
内部字段：
{
    path : '/demo/a',
    params : {"key1":"value1","key2":"value2"}
}

#### 示例代码

```js
var moblink = api.require('moblinkpro');
moblink.restoreScene(function(ret){
    var msg = "path:" + ret.path + "\nparams:" + JSON.stringify(ret.params);
    // ret.path为场景信息中携带的路径
    // ret.params为场景信息中携带的参数
});
```

#### 可用性

iOS系统，Android系统

可提供的1.0.3及更高版本