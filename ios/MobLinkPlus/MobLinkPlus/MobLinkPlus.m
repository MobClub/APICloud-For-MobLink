//
//  MobLinkPlus.m
//  MobLinkPlus
//
//  Created by 李树志 on 2019/7/29.
//  Copyright © 2019 李树志. All rights reserved.
//


#import "MobLinkPlus.h"

#import "NSDictionaryUtils.h"

#import <MobLinkPro/MobLink.h>

#import <MobLinkPro/MLSDKScene.h>

typedef void(^MLRestoreResult)(NSDictionary *);

@interface MobLinkPlus() <IMLSDKRestoreDelegate>

@property (copy, nonatomic) MLRestoreResult restoreResult;

@end

@implementation MobLinkPlus

- (void)IMLSDKWillRestoreScene:(MLSDKScene *)scene Restore:(void (^)(BOOL, RestoreStyle))restoreHandler
{
    NSString *path = scene.path;
    NSString *mobid = scene.mobid;
    NSString *rawURL = scene.rawURL;
    NSString *className = scene.className;
    NSDictionary *params = scene.params;
    
    NSMutableDictionary *mDic = [NSMutableDictionary dictionary];
    if (path)
    {
        [mDic addEntriesFromDictionary:@{@"path" : path}];
    }
    if (mobid)
    {
        [mDic addEntriesFromDictionary:@{@"mobid" : mobid}];
    }
    if (rawURL)
    {
        [mDic addEntriesFromDictionary:@{@"rawURL" : rawURL}];
    }
    if (className)
    {
        [mDic addEntriesFromDictionary:@{@"className" : className}];
    }
    if (params)
    {
        [mDic addEntriesFromDictionary:@{@"params" : params}];
    }
    
    if (self.restoreResult)
    {
        self.restoreResult(mDic);
    }
    restoreHandler(NO, MLDefault);
}

#pragma mark - Override
+ (void)onAppLaunch:(NSDictionary *)launchOptions {
    // 方法在应用启动时被调用
}

- (id)initWithUZWebView:(UZWebView *)webView {
    if (self = [super initWithUZWebView:webView]) {
        // 初始化方法
        // ...
    }
    return self;
}

- (void)dispose {
    // 方法在模块销毁之前被调用
}

#pragma mark - js methods

JS_METHOD(getMobId:(UZModuleMethodContext *)context) {
    NSDictionary *param = context.param;
    MLSDKScene *scene = [MLSDKScene sceneForPath:param[@"path"] params:param[@"params"]];
    [MobLink getMobId:scene result:^(NSString *mobid, NSString *domain, NSError *error) {
        if (error)
        {
            NSDictionary *dic = @{
                                  @"error" : [self _covertError:error]
                                  };
            [context callbackWithRet:nil err:dic delete:YES];
            return;
        }
        if (mobid && domain)
        {
            NSDictionary *dic = @{@"mobid" : mobid,@"domain": domain};
            [context callbackWithRet:dic err:nil delete:YES];
        }
    }];
}

JS_METHOD(restoreScene:(UZModuleMethodContext *)context) {
    
    [MobLink setDelegate:self];
    
    self.restoreResult = ^(NSDictionary *dic) {
        [context callbackWithRet:dic err:nil delete:NO];
    };
}

#pragma mark - private methods

- (id)_covertError:(NSError *)error
{
    if (error)
    {
        return @{@"code":@(error.code),@"userInfo":error.userInfo?:@{}};
    }
    
    return [NSNull null];
}

@end

