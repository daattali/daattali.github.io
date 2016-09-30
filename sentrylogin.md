---
---

					
<!-- BEGIN SENTRY AJAX LOGIN FORM - "Flat and Fresh" version 1.0 (Beta Build D) - Member Login Code Copyright © Sentrylogin.com, Shawn, Co. https://www.sentrylogin.com --> 

<meta http-equiv="CACHE-CONTROL" content="NO-CACHE">
<meta http-equiv="PRAGMA" content="NO-CACHE"> 
<link rel="stylesheet" type="text/css" href="https://www.sentrylogin.com/sentry/scripts/CSS_flat.css"> 
<script type="text/javascript" src="https://www.sentrylogin.com/sentry/scripts/Sentry_AJAX_Style2014.js">
</script> 

<div id="Settings" manner="NORMAL" showSignUp="YES" style="display:none;"></div>

<!-- IT'S EASY TO MAKE CHANGES! 

We've added the text "Member Login" but you can use any wording. You can also use different images by changing "src" in the img tags to bring in your own, or NO image just by deleting the whole <img> tag.

If your site is dark and you'd prefer white icons, just replace the img "icon_loginDoor.png" with "icon_loginDoor_white.png" and the img "icon_hamburger_black.png" with "icon_hamburger_white.png"

Also, if your site is dark, be sure to change the text color in the spans "SentryTrigger" and "SentryHello" by changing "color: Black;" to "color: White;" (or any light color).

We've deliberately put the styling directly in the tags here so you can easily change fonts, colors, sizes, etc., or just remove the "style" attribute and allow your web page to apply its style automatically.

-->

<span id="SentryTrigger" class="handCursor" onClick="SentryPopUp();" title="Click here to log in." style="color: Black; font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 18px; font-weight: normal;"> Member Login <img src="https://www.sentrylogin.com/sentry/images/icon_loginDoor.png" align="absmiddle" alt="Member Login"> </span>

<span id="SentryHello" class="handCursor" style="color: Black; font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 14px; font-weight: normal; display: none;"> <span onClick="SentryPopUp(1);" title="Click to enter the member area, make changes to your membership, and more.">Hi, <span id="helloName">(First Name)</span> <img src="https://www.sentrylogin.com/sentry/images/icon_hamburger_black.png" align="absmiddle" alt="Member Area"></span> | <span onClick="LogOut();" style="padding-left: 3px;">Log Out</span> 

</span> <div id="SentryOverlay">



<div id="SentryOutermost" class="before">

<div id="SentryLoginBox" style="background-color: DarkSeaGreen;border-radius: 4px;">

<div id="SentryTitleBar" style="background-color: DarkSeaGreen;border-top-left-radius: 4px; border-top-right-radius: 4px;">

<div id="iconKey" style=""> </div>

<div id="Title" style="color: White;font-family: Rockwell, 'Roboto Slab', Bitter, 'Courier Bold', Courier, Georgia, Times, 'Times New Roman', serif;font-size: 22px;">Member Login</div> 

<div id="Welcome" style="color: White;font-family: Rockwell, 'Roboto Slab', Bitter, 'Courier Bold', Courier, Georgia, Times, 'Times New Roman', serif;font-size: 22px;">Welcome, <span id="welcomeBarName">(First Name)</span>!</div> </div>

<div id="SentryFormGroup"> <form name="Sentry_login_form" onSubmit="sentryLogin();">

<div id="SentryInputGroup" style="height: 79px; width: 280px; border-radius: 7px; border-color: #FFFFFF;  background-color: #DCDCDC;">

<div id="iconEmail"> </div> <input type="text" id="Sentry_email" onFocus="Sentry_onfocus(this.id);" onKeyDown="Sentry_onkeydown(event, this.id);" onKeyUp="Sentry_onkeyup(this.value, this.id, event);" onBlur="Sentry_onblur(this.value, this.id);" value="E-mail Address" tabindex="1" style="font-weight: normal;  border-top-right-radius: 6px;background-color: #EDEDF1; font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif;font-size: 16px;color: Gray;">

<hr class="clear" id="inputsSep" style=" background-color: #FFFFFF !important;  display: block;">

<div id="iconPassword"> </div> <div id="iconShowPassword"> </div> <input type="text" id="Sentry_password" onFocus="Sentry_onfocus(this.id);" onKeyDown="Sentry_onkeydown(event, this.id);" onKeyUp="Sentry_onkeyup(this.value, this.id, event);" onBlur="Sentry_onblur(this.value, this.id);" value="Password" tabindex="2" style="font-weight: normal;  border-bottom-right-radius: 6px;background-color: #EDEDF1; font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif;font-size: 16px;color: Gray;"> </div>

<span id="forgotSpan" onClick="Sentry_onClick(this.id);" lnk="https://www.sentrylogin.com/sentry/sentry_remind_pw.asp?Sentry_ID=19301" tabindex="0" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 13px; color: #DDDDDD;">Forgot?</span> 

<input type="checkbox" id="unHide" style="height:0px; width:0px; margin:0px; display: none;" value="1"><span id="unHideSpan" onClick="Sentry_onClick(this.id);" class="handCursor" title="Do not reveal password in public" tabindex="0" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 13px;color: #DDDDDD; ">Show</span> <span id="Stay" onKeyDown="Sentry_onkeydown(event, this.id);" onKeyUp="Sentry_onkeyup(this.value, this.id, event);" tabindex="3">

<input name="" type="checkbox" value="" id="psist" class="SentryCss-checkbox"><label id="psistLabel" for="psist" class="SentryCss-label" style="font-weight: normal; font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 13px;color: #DDDDDD; ">Stay Logged In</label></span>

<div class="SentryLoginButton" id="Sentry_button" onClick="Sentry_onClick(this.id);" onKeyDown="Sentry_onkeydown(event, this.id);" onKeyUp="Sentry_onkeyup(this.value, this.id, event);" onblur="Sentry_onblur(this.value, this.id);" tabindex="4" style="border-radius: 2px;"><span id="lgnBtnText" style="font-family: Rockwell, 'Roboto Slab', Bitter, 'Courier Bold', Courier, Georgia, Times, 'Times New Roman', serif;font-size: 20px;">Log In</span></div> 
</form> </div> 

<style>
 .SentryLoginButton {

	background-color: #DCDCDC;

	color: #848484;
	
 }
 .SentryLoginButton:hover {

	background-color: #848484;

	color: #CCCCCC;

}
</style>

<div id="SentryHelloGroup">



<div id="EnterMember" onClick="Sentry_onClick(this.id);" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 16px;color: #FFFFFF; ">Enter Member Area</div>



<div id="messages" style="display: none; font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 16px;color: #FFFFFF; "> <span id="msgSpan">(Message automatically replaces this text)</span>

<div class="SentryLoginButton" id="msgOkBtn" onClick="Sentry_onClick(this.id);" onKeyDown="Sentry_onkeydown(event, this.id);" onKeyUp="Sentry_onkeyup(this.value, this.id, event);" tabindex="4" style="border-radius: 2px;"><span id="msgOkBtnText" style="font-family: Rockwell, 'Roboto Slab', Bitter, 'Courier Bold', Courier, Georgia, Times, 'Times New Roman', serif;font-size: 20px;">OK</span></div> 
</div>

<div id="wobbler"> </div> </div>

<div id="SentryFooterBar">



<div id="iconProfile"> </div><span id="myProfile" onClick="Sentry_onClick(this.id);" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 16px;color: #FFFFFF;">My Profile</span>



<span id="signUpLnk" onClick="Sentry_onClick(this.id);" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 16px;color: #FFFFFF;">Not a member? Sign up.</span>

<span id="txtLogOut" onClick="LogOut();" style="font-family: Tahoma, Geneva, Arial, sans-serif;font-size: 16px;color: #FFFFFF;">Log Out</span><div id="xout" onClick="Sentry_onClick(this.id);" title="Close"> </div> </div> </div> </div> 

</div> 

<input id="Sentry_ID" type="hidden" value="19301"> 

<input id="univ" type="hidden" value="1">
 
<script> initializeSentry(); </script>

<!-- END SENTRY AJAX LOGIN FORM --> 
  
