WheresMyAndroid

12/3/2010

Find your phone when you lose it.  Use an SMS message to reply
with the GPS location of the phone, or ring the phone loudly.

This Android application waits for an SMS message containing a user
specified keyword and command.  The commands are listed below. The
case of the keyword and command is ignored (it can be mixed upper and lower case).
The keyword may appear anywhere in the SMS message.  It doesn't have
to be the first thing in the message.  If there is only a keyword with
no recognized command, the "location" command is used by default.

After installing this application, run it to configure it.  Make
sure to test your configuration by sending yourself a message
with the keyword.  If you incorrectly configure the keyword, you won't
be able to look it up if you lose your phone.

<keyword> location

  This command replies with the phone's GPS location.  Use this
  command to find your phone if you have no idea where you left
  it.  For example if you might have left it in a store or
  movie theater. 
  
  Copy the latitude and longitude to google maps to see where your phone is.

  The location is determined using GPS.  GPS is used only for a moment
  when generating a reply, saving power.  (I leave my GPS enabled
  all the time.)

<keyword> ring

  Takes the phone out of vibrate or silent mode, turns the ringer
  volume to its loudest, and rings the phone.  Use this command if you
  know you've left the phone in the house, but you just can't find it.

Send an SMS message from email

  How do you send an SMS message if you've lost your phone?  Use your mobile
  provider's email to SMS gateway.  For T-mobile this is:

    phone_number_with_area_code_no_spaces@tmomail.net

  SMS gateways can mess with the formatting of a message.  This is why
  the keyword is allowed to appear anywhere in the message.  This lets
  you put the keyword and command in either the subject or the body
  of the email.

Does not require data service

  WheresMyAndroid was written to work with an Android that does not have
  the data service enabled.  (A rare situation, but one I'm in.) Thus there
  is no attempt made to transform the latitude and longitude to a street
  address and send it in the SMS reply.  Such a transformation requires
  location services to contact servers on the internet.

  Although written for a phone without a data service, WheresMyAndroid
  will attempt to fall back to using cell tower based location when
  GPS is not available.  But this does require the data service to 
  be enabled.
 
  
Tested on MyTouch Slide with Android 2.1
