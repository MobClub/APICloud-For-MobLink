//
//  UZModuleDemo.m
//  UZModule
//
//  Created by kenny on 14-3-5.
//  Copyright (c) 2014年 APICloud. All rights reserved.
//

#import "UZModuleDemo.h"
#import "NSDictionaryUtils.h"

@implementation UZModuleDemo

#pragma mark - Override
+ (void)onAppLaunch:(NSDictionary *)launchOptions {
    // 方法在应用启动时被调用
}

- (id)initWithUZWebView:(UZWebView *)webView {
    if (self = [super initWithUZWebView:webView]) {
        // 初始化方法
    }
    return self;
}

- (void)dispose {
    // 方法在模块销毁之前被调用
}

#pragma mark - js methods
/**
 普通方法，方法会在主线程执行，结果通过回调的方式回传js，方法名以jsmethod_作为前缀，如：- (void)jsmethod_showAlert:(UZModuleMethodContext *)context，为了方便一般使用JS_METHOD宏来定义
 */
JS_METHOD(showAlert:(UZModuleMethodContext *)context) {
    NSDictionary *param = context.param;
    NSString *title = [param stringValueForKey:@"title" defaultValue:nil];
    NSString *msg = [param stringValueForKey:@"msg" defaultValue:nil];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:msg preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSDictionary *ret = @{@"index":@(1)};
        [context callbackWithRet:ret err:nil delete:YES];
    }]];
    [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSDictionary *ret = @{@"index":@(2)};
        [context callbackWithRet:ret err:nil delete:YES];
    }]];
    [self.viewController presentViewController:alert animated:YES completion:nil];
}

/**
 同步方法，结果直接以return的方式返回给js，方法名以jsmethod_sync_作为前缀，如：- (id)jsmethod_sync_systemVersion:(UZModuleMethodContext *)context，为了方便一般使用JS_METHOD_SYNC宏来定义
 */
JS_METHOD_SYNC(systemVersion:(UZModuleMethodContext *)context) {
    return [UIDevice currentDevice].systemVersion;
}


/* 旧版本实现方式，开放给js调用的方法需要在module.json里面先进行配置。这种方式现已被弃用，推送使用JS_METHOD宏的方式来定义方法。
- (void)showAlert:(NSDictionary *)param {
    NSInteger cbId = [param integerValueForKey:@"cbId" defaultValue:0];
    NSString *title = [param stringValueForKey:@"title" defaultValue:nil];
    NSString *msg = [param stringValueForKey:@"msg" defaultValue:nil];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:msg preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSDictionary *ret = @{@"index":@(1)};
        [self sendResultEventWithCallbackId:cbId dataDict:ret errDict:nil doDelete:YES];
    }]];
    [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSDictionary *ret = @{@"index":@(2)};
        [self sendResultEventWithCallbackId:cbId dataDict:ret errDict:nil doDelete:YES];
    }]];
    [self.viewController presentViewController:alert animated:YES completion:nil];
}

- (NSString *)systemVersion:(NSDictionary *)param {
    return [UIDevice currentDevice].systemVersion;
}
 */

@end
