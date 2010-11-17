WheresMyAndroid

11/16/2010

Find your phone when you lose it.

This Android application waits for an SMS message containing a user
specified keyword and replies with the latitude and longitude.  Copy 
the latitude and longitude to google maps to see where your phone is.

The location is determined using GPS.  GPS is used only for a moment
when generating a reply, thus saving power.  So I leave my GPS enabled
all the time.

WheresMyAndroid was written to work with an Android that does not have
the data service enabled.  (A rare situation, but one I'm in) Thus there
is no attempt made to transform the latitude and longitude to a street
address and send it in the SMS reply.  Such a transformation requires
location services to contact servers on the internet.

Although written for a phone without a data service, WheresMyAndroid
will attempt to fall back to using cell tower based location when
GPS is not available.  But this does require the data service to 
be enabled.

How do you send an SMS message if you've lost your phone?  Use your mobile
provider's email to SMS gateway.  For t-mobile this is:

   phone_number_with_area_code_no_spaces@tmomail.net
   
After installing it, run WheresMyAndroid to configure it.  
  
Tested on MyTouch Slide with Android 2.1
