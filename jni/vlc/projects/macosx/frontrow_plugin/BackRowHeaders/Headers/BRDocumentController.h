/*
 *     Generated by class-dump 3.1.1.
 *
 *     class-dump is Copyright (C) 1997-1998, 2000-2001, 2004-2006 by Steve Nygard.
 */

#import <BackRow/BRController.h>

@class BRButtonControl, BRHeaderControl, BRVerticalScrollControl;

@interface BRDocumentController : BRController
{
    BRHeaderControl *_header;
    BRVerticalScrollControl *_verticalScroll;
    BRButtonControl *_button;
}

- (id)init;
- (void)dealloc;
- (void)setButtonTitle:(id)fp8 action:(SEL)fp12 target:(id)fp16;
- (void)setHeaderTitle:(id)fp8;
- (struct CGSize)_scrollSizeForMasterFrame:(struct CGRect)fp8;
- (void)setHeaderIcon:(id)fp8 horizontalOffset:(float)fp12 kerningFactor:(float)fp16;
- (void)setDocumentPath:(id)fp8 encoding:(unsigned int)fp12;
- (void)setDocumentPath:(id)fp8;
- (void)setFrame:(struct CGRect)fp8;

@end
