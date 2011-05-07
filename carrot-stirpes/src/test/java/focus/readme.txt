On 8 March 2011 09:38, Bill van Melle <bill.van.melle@gmail.com> wrote:

> > When I played around with your example, substituting a focusable component
> > (TextInput) for ImageView in one of the displays, or making the displays
> > host a single, maximized window (my standard use case), I got even weirder
> > behavior, such as input going to one subwindow even after clicking the frame
> > title of a different subwindow or even the other display.  See the attached
> > revision of your App file.
> >

Bill,

I just looked at your version of the app and believe that it is behaving as
Pivot intended.  If there is a focused Pivot Component, it will receive
keyboard events.  Pivot does not track active Windows and focused Components
separately for each Display.

So in your scenario, the Pivot Application as a whole has 'focus' (via
either of the native OS windows being active), and there is a focused Pivot
Component (a TextInput, perhaps in the other OS window), so that receives
the events.

The default skin for TextInput (TerraTextInputSkin) does not consume
keyTyped events, so these are technically 'unhandled' and then picked up by
the defined Application.UnprocessedKeyHandler.  In this case, that handler
is a trivial one which doesn't care whether or not there is a focused
Component, so it just dumps to SysOut.  (As mentioned before, the app was
intended to show catching keyboard events when there is not a focused
Component)

I want to reiterate that this example was intended to show that keyboard
handling in Pivot might involve a little more that just adding
ComponentKeyListeners due to the rules that determine where events are sent.
 However if you put some simple logic into a custom UnprocessedKeyHandler
you could ensure that it wont process anything if there is a focused
Component, or perhaps that it won't process if the focused Component is not
in the active OS window.

You could also create some kind of 'UnprocessedKeyHandlerList' class, expose
it through your Application instance, and allow multiple
UnprocessedKeyHandlers to be added/removed at runtime.
 The UnprocessedKeyHandlerList could have whatever logic you need to process
events for your specific requirements, or even accept some sort of
filter/qualifier class which would contain the logic, and therefore be
easily tweaked.

Anyway, I think this thread has served its purpose now (handling keyboard
events for Pivot Components that are defined as non-focusable).  It might be
best to begin a new thread about component focus, active windows, multiple
displays etc for whatever further issues, questions or suggestions you might
have.

Chris

