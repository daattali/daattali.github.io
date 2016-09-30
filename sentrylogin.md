---
---

<!-- BEGIN SENTRY AJAX LOGIN FORM - version 3.1 (Beta Build A) - Member Login Code Copyright © Sentrylogin.com, Shawn, Co. https://www.sentrylogin.com -->
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<style>
.handCursor {
cursor:pointer;
cursor:hand;
} 
#magicGroup form {
margin: 0;
padding: 0; 
}
#magicGroup A:link {text-decoration: none; color:black;}
#magicGroup A:visited {text-decoration: none; color:black;}
#magicGroup A:active {text-decoration: none; color:black;}
#magicGroup A:hover {text-decoration: none; color:black;}
.clearfix:after{
  content:".";display:block;height:0;clear:both;visibility:hidden;
}
  .spacer0{clear:both; height:0px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer{clear:both; height:1px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer2{clear:both; height:2px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer3{clear:both; height:3px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer4{clear:both; height:4px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer5{clear:both; height:5px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer10{clear:both; height:10px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  .spacer20{clear:both; height:20px; margin:0px; padding:0px; line-height:0px; font-size:0px;}
  </style>
  <script type="text/javascript" src="https://www.sentrylogin.com/sentry/scripts/Sentry_AJAX.js"></script>
  <div id="Settings" ALIGNMENT="CENTER" MANNER="GROW" style="display:none;"></div>
  
  <div id="Sentry_outermost" style="width:175px; position:relative;"> 
  
  
  <form name="Sentry_login_form" onsubmit="sentryLogin();">
  <div style="height:1px;"><a href="https://www.sentrylogin.com"><img src="https://www.sentrylogin.com/sentry/images/logo.gif" alt="Sentry Password Protection Member Login" width="2" height="1" border="0"></a></div>
  <div id="TopBar" onclick="Sentry_onClick(this.id);" class="handCursor" style="width:175px; background-color:White; padding-top:2px; padding-bottom:2px; border-width:1px; border-style:solid; border-color:LightGray; border-radius: 6px;">
  <div id="Sentry_label" style="text-align: right; font-family:sans-serif; font-size:12px; font-weight:bold; margin-bottom:3px;"><span id="Sentry_label_span" style="padding-right:10px; padding-left:5px;">Member Login</span><span id="downArrow"><img src="https://www.sentrylogin.com/sentry/images/down_arrow.png" align="baseline" style="margin-right:5px;"></span></div></div>
  <div id="magicGroup" style="z-index: 10000; background-color:white; text-align:left; position:absolute; left: -9999px; width:175px; padding-top:15px; border-style:solid; border-color:LightGray; border-top-width:0px; border-right-width:1px; border-bottom-width:1px; border-left-width:1px; border-radius: 6px;">
  <div id="messages" style="display:none; padding-left:15px; padding-right:15px; padding-bottom:15px; font-family: Arial; font-size:12px; color: red;">.</div>
  <div id="Sentry_emailDiv" style="width:146px; margin-left:15px;"><span style="text-align:left;"> 
  <input type="text" id="Sentry_email" style="padding-left: 3px; border-style:solid; border-width:1px; border-color:LightGray; border-radius: 6px; width:146px; height:24px;" onfocus="Sentry_onfocus(this.id);" onkeydown="Sentry_onkeydown(event, this.id);" onkeyup="Sentry_onkeyup(this.value, this.id, event);" onblur="Sentry_onblur(this.value, this.id);" value="E-mail Address"></span></div>
  <div id="Sentry_passwordDiv" style="width:146px; margin-left:15px; margin-top:2px;"><span style="text-align:left;"> 
  <input type="text" id="Sentry_password" style=" padding-left: 3px; border-style:solid; border-width:1px; border-color:LightGray; border-radius: 6px; width:146px; height:24px;" onfocus="Sentry_onfocus(this.id);" onkeydown="Sentry_onkeydown(event, this.id);" onkeyup="Sentry_onkeyup(this.value, this.id, event);" onblur="Sentry_onblur(this.value, this.id);" value="Password"></span> </div>
  <div id="Sentry_HIDpasswordDiv" style="width:146px; display: none; margin-left:15px; margin-top:2px;"><span style="text-align:left;">
  <input type="password" id="Sentry_HIDpassword" style=" padding-left: 3px; border-style:solid; border-width:1px; border-color:LightGray; border-radius: 6px; width:146px; height:24px;" onfocus="Sentry_onfocus(this.id);" onkeydown="Sentry_onkeydown(event, this.id);" onkeyup="Sentry_onkeyup(this.value, this.id, event);" onblur="Sentry_onblur(this.value, this.id);"></span></div>
  <div id="unHideDiv" style="margin-bottom:5px; font-family:Arial; font-size:10px;"><span id="forgotSpan" onclick="Sentry_onClick(this.id);" style="vertical-align:top; margin-left:15px; font-family:Arial; font-size:10px; color:gray;" class="handCursor">Forgot?</span> 
  <input type="checkbox" id="unHide" style="height:12px; width:10px; margin:4px; display:none;" value="1">
  <span id="unHideSpan" style="width:25px; vertical-align:top; font-family: Arial;font-size:10px; color:gray; margin-left:78px;" onclick="Sentry_onClick(this.id);" class="handCursor" title="Do not reveal password in public">Show</span></div>
  <div id="buttonDiv" style="float:left; width:45px; margin-left:15px;"> 
  
  <button type="button" onclick="sentryLogin();" style="height:25px; padding:2; width:45px; font-family:Arial; font-size:11px;" id="Sentry_button">Go</button></div>
  <div id="psistDiv" style="float:left; width:98px; text-align:right; margin-top:10px; margin-right:15px;"> 
  <input id="psist" type="checkbox" value="1" style="height:10px; width:10px; margin:0; margin-bottom:2px; margin-right:2px;" class="handCursor" title="Do not use when in public" onclick="Sentry_onClick(this.id);">
  <span id="psistSpan" style="font-family: Arial;font-size:10px; color:gray;" onclick="Sentry_onClick(this.id);" class="handCursor" title="Do not use when in public">Stay Logged In</span></div>
  <div class="spacer0"> </div>
  <div class="spacer10"> </div>
  <div id="goInside" style="font-family: Arial;font-size:12px; text-align: right; margin-top:10px; margin-right:15px; margin-bottom:5px; margin-left:15px; display:none;"><span id="goInsideSpan" class="handCursor" style="font-weight:bold;">.</span></div>
  <div id="myProfile" onclick="Sentry_onClick(this.id);" style="font-family: Arial;font-size:12px; font-weight:bold; text-align: right; margin-top:2px; margin-right:15px; margin-left:15px;" class="handCursor">My Profile</div>
  <div id="signUp" style="font-family: Arial;font-size:12px; font-weight:bold; text-align: right; margin-top:2px; margin-right:15px; margin-bottom:5px; margin-left:15px;"><a href="https://www.sentrylogin.com/sentry/member_signup_list.asp?Site_ID=19301" title="Click to Subscribe Now">Sign Up</a> </div>
  <div id="logOut" style="font-family: Arial;font-size:12px; font-weight:bold; text-align: right; margin-top:2px; margin-right:15px; margin-bottom:5px; margin-left:15px; display:none;"><span class="handCursor" onClick="LogOut();">Log Out</span></div>
  <div class="spacer0"> </div>
  <div id="xbox" style="margin-top:12px;"><img id="xout" onclick="Sentry_onClick(this.id);" src="https://www.sentrylogin.com/sentry/images/x.png" style="margin:2px; margin-right:9px;" class="handCursor"></div>
  <div class="spacer5"> </div>
  </div>
  <input id="Sentry_ID" type="hidden" value="19301">
  <input id="univ" type="hidden" value="1">
  <div id="Sentry_noJSLogin" style="width:175px; background-color:white;"><span style="color:red; font-family: Arial;font-size:12px; font-weight:bold;">Javascript Required</span></div>
  </form>
  </div>
  <div id="Sentry_loggingIn" style="width:175px; display:none;">
  <img src="https://www.sentrylogin.com/sentry/images/wait_animated_sentry.gif">
  </div>
  <div id="Sentry_In" style="display:none;">For testing.
  </div>
  <script language="JavaScript" type="text/JavaScript">initializeSentry(); 
  </script>
  <noscript>You must have JavaScript enabled in order to log in.</noscript>
  <!-- END SENTRY AJAX LOGIN FORM -->
