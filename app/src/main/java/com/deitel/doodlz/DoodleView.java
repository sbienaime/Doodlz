// DoodleView.java
// Main View for the Doodlz app.
package com.deitel.doodlz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

// custom View for drawing
public class DoodleView extends View {
   // used to determine whether user moved a finger enough to draw again
   private static final float TOUCH_TOLERANCE = 10;

   private Bitmap bitmap; // drawing area for displaying or saving
   private Canvas bitmapCanvas; // used to to draw on the bitmap
   private final Paint paintScreen;// used to draw bitmap onto screen
   public static Paint paintShape;
   private final Paint paintLine; // used to draw lines onto bitmap
   public static boolean oval_selected;
   public static boolean rectangle_selected;
   public static int i =0;
   public static Rect rect=new Rect();
   public static boolean regular_sketch;
   // Maps of current Paths being drawn and Points in those Paths
   private final Map<Integer, Path> pathMap = new HashMap<>();
   private final Map<Integer, Point> previousPointMap =  new HashMap<>();

   // DoodleView constructor initializes the DoodleView
   public DoodleView(Context context, AttributeSet attrs) {
      super(context, attrs); // pass context to View's constructor
      paintScreen = new Paint(); // used to display bitmap onto screen

      // set the initial display settings for the painted line
      paintShape = new Paint();
      paintShape.setAntiAlias(true);
      paintShape.setColor(Color.BLACK);
      paintShape.setStyle(Paint.Style.STROKE);
      paintShape.setStrokeCap(Paint.Cap.ROUND);


      paintLine=new Paint();
      paintLine.setAntiAlias(true); // smooth edges of drawn line
      paintLine.setColor(Color.BLACK); // default color is black
      paintLine.setStyle(Paint.Style.STROKE); // solid line


      //paintLine.setStrokeWidth(5); // set the default line width
      paintLine.setStrokeCap(Paint.Cap.ROUND); // rounded line ends
   }

   // creates Bitmap and Canvas based on View's size
   @Override
   public void onSizeChanged(int w, int h, int oldW, int oldH) {
      bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
              Bitmap.Config.ARGB_8888);
      bitmapCanvas = new Canvas(bitmap);
      bitmap.eraseColor(Color.WHITE); // erase the Bitmap with white
   }
   public void loadBackground( int selection)
   { switch(selection) {

      case 1:
         Resources res1 = getResources();
         Bitmap bitmap1 = BitmapFactory.decodeResource(res1, R.drawable.lotus);
         bitmapCanvas.drawBitmap(bitmap1, 0, 0, paintScreen);
         break;
      case 2:
         Resources res2 = getResources();
         Bitmap bitmap2 = BitmapFactory.decodeResource(res2, R.drawable.banksy);
         bitmapCanvas.drawBitmap(bitmap2, 0, 0, paintScreen);
         break;
      case 3:
         Resources res3 = getResources();
         Bitmap bitmap3 = BitmapFactory.decodeResource(res3, R.drawable.fashion_accessories);
         bitmapCanvas.drawBitmap(bitmap3, 0, 0, paintScreen);
         break;
      case 4:
         Resources res4 = getResources();
         Bitmap bitmap4 = BitmapFactory.decodeResource(res4, R.drawable.african_art);
         bitmapCanvas.drawBitmap(bitmap4, 0, 0, paintScreen);
         break;
      case 5:
         Resources res5 = getResources();
         Bitmap bitmap5 = BitmapFactory.decodeResource(res5, R.drawable.fallleaves);
         bitmapCanvas.drawBitmap(bitmap5, 0, 0, paintScreen);
         break;
      case 6:
         Resources res6 = getResources();
         Bitmap bitmap6 = BitmapFactory.decodeResource(res6, R.drawable.rainbow_parrot);
         bitmapCanvas.drawBitmap(bitmap6, 0, 0, paintScreen);
         break;

   }
   }

   // clear the painting
   public void clear() {
      pathMap.clear(); // remove all paths
      previousPointMap.clear(); // remove all previous points
      bitmap.eraseColor(Color.WHITE); // clear the bitmap
      invalidate(); // refresh the screen
   }

   public void setBackgroundColor(int color)
   {
      bitmap.eraseColor(color);
   }

   // set the painted line's color
   public void setDrawingColor(int color) {
      paintLine.setColor(color);
      paintShape.setColor(color);
   }

   // return the painted line's color
   public int getDrawingColor() {
      return paintLine.getColor();

   }

   public void setShapeWidth(int width) {
      paintShape.setStrokeWidth(width);


   }


   public int getShapeWidth() {
      return (int) paintShape.getStrokeWidth();

   }

   // set the painted line's width
   public void setLineWidth(float width) {
      Log.i("WidthValueBefore",width+"");
      if (width >0 & width <= 0.015686275)
      {width = 10;}

      //  else if (width >0.07843138 & width <=0.01176470 )
      //  {width =22 ;}

      //else if (width >0.01176470 & width <=  )
      // {width = 24;}

      else if (width > 0.015686275 & width <=  0.027450982)
      {width = 80;}

      // else if (width > 0.019607844& width <= 0.023529414)
      //{width = 28;}

      //else if (width > 0.023529414 & width <= 0.027450982)
      //{width = 30;}

      else if (width >0.027450982)
      {width =120 ;}

      // else if (width >0.03137255 & width <= 0.03529412)
      //{width = 34;}

      //   else if (width >0.03529412 & width < 0.04705883)
      //  {width = 40;}



      Log.i("WidthValue",width+"");// For Debugging , checking variable value

      paintLine.setStrokeWidth(width);


   }

   // return the painted line's width
   public int getLineWidth() {
      return (int) paintLine.getStrokeWidth();

   }

   // perform custom drawing when the DoodleView is refreshed on screen
   @Override
   protected void onDraw(Canvas canvas) {

      // draw the background screen
      canvas.drawBitmap(bitmap, 0, 0, paintScreen);

      // for each path currently being drawn
      for (Integer key : pathMap.keySet())

         canvas.drawPath(pathMap.get(key), paintLine); // draw line
   }


    public static  void setToFill(){

      paintShape.setStyle(Paint.Style.FILL);

   }


   public static  void setToStroke(){

      paintShape.setStyle(Paint.Style.STROKE);

   }
   // handle touch event
   @Override
   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getActionMasked(); // event type
      int actionIndex = event.getActionIndex(); // pointer (i.e., finger)
      if  (rectangle_selected){

       if (action == MotionEvent.ACTION_DOWN ||
                 action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));

            i++;
            regular_sketch=false;
            switch (i) {
               case 1:
                  rect.left = (int) event.getX(actionIndex);
                  break;
               case 2:
                  rect.right = (int) event.getX(actionIndex);
                  break;
               case 3:
                  rect.top = (int) event.getY(actionIndex);
                  break;
               case 4:
                  rect.bottom = (int) event.getY(actionIndex);
                  rectangle_selected = false;
                  break;
            }
            if (i == 4){
               RectShape r = new RectShape();

               bitmapCanvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paintShape);
               setToStroke();
               i=0;
         }
            Log.i("BOOLEAN VALUE", rectangle_selected+"");
            Log.i("ValueOfit",i+"");
         }
            //setLineWidth(event.getSize());

      }
      else { regular_sketch = true;}

      if(oval_selected){

         regular_sketch=false;

         if (action == MotionEvent.ACTION_DOWN ||
                 action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));


             i++;

            switch (i) {
               case 1:
                  rect.left = (int) event.getX(actionIndex);
                  break;
               case 2:
                  rect.top = (int) event.getY(actionIndex);
                  break;
               case 3:
                  rect.right =(int) event.getX(actionIndex);
                  break;
               case 4:
                  rect.bottom = (int) event.getY(actionIndex);
                  oval_selected = false;


            }
            if (i == 4){
               OvalShape oval;
               RectShape rect1;
               bitmapCanvas.drawOval(rect.left, rect.top,rect.right,rect.bottom, paintShape);
               i=0;
            }
            Log.i("BOOLEAN VALUE", rectangle_selected+"");
            Log.i("ValueOfit",i+"");
         }
         //setLineWidth(event.getSize());


         System.out.println("Testing");
      regular_sketch=false;}
      else {regular_sketch=true;}


      if (regular_sketch){


         // determine whether touch started, ended or is moving
         if (action == MotionEvent.ACTION_DOWN ||
                 action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
            setLineWidth(event.getSize());

         } else if (action == MotionEvent.ACTION_UP ||
                 action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
         } else {

            touchMoved(event);
         }
      }
      invalidate(); // redraw
      return true;
   }


   private void RectangleSelected(int x , int y, int movementID  ){

      Path path; // used to store the path for the given touch id
      Point point; // used to store the last point in path

      // if there is already a path for MovementID
      if (pathMap.containsKey(movementID)) {
         path = pathMap.get(movementID); // get the Path
         path.reset(); // resets the Path because a new touch has started
         point = previousPointMap.get(movementID); // get Path's last point
      }
      else {
         path = new Path();
         pathMap.put(movementID, path); // add the Path to Map
         point = new Point(); // create a new Point
         previousPointMap.put(movementID, point); // add the Point to the Map
      }

      // move to the coordinates of the touch
      path.moveTo(x, y);
      point.x = (int) x;
      point.y = (int) y;

   }

   // called when the user touches the screen
   private void touchStarted(float x, float y, int lineID) {
      Path path; // used to store the path for the given touch id
      Point point; // used to store the last point in path

      // if there is already a path for lineID
      if (pathMap.containsKey(lineID)) {
         path = pathMap.get(lineID); // get the Path
         path.reset(); // resets the Path because a new touch has started
         point = previousPointMap.get(lineID); // get Path's last point
      }
      else {
         path = new Path();
         pathMap.put(lineID, path); // add the Path to Map
         point = new Point(); // create a new Point
         previousPointMap.put(lineID, point); // add the Point to the Map
      }

      // move to the coordinates of the touch
      path.moveTo(x, y);
      point.x = (int) x;
      point.y = (int) y;
   }




//#moveRect
   private static void moveRect(RectShape SelectedRectangle){







   }


  //tracks user finger in order to move rectangle
   private void RectangleMoved(MotionEvent event) {
      // for each of the pointers in the given MotionEvent

      for (int i = 0; i < event.getPointerCount(); i++) {

         Log.i("PRESSURE", event.getSize()+"");
         // get the pointer ID and pointer index
         int pointerID = event.getPointerId(i);
         int pointerIndex = event.findPointerIndex(pointerID);

         // if there is a path associated with the pointer
         if (pathMap.containsKey(pointerID)) {

            // get the new coordinates for the pointer
            float newX = event.getX(pointerIndex);
            float newY = event.getY(pointerIndex);

            // get the path and previous point associated with
            // this pointer
            Path path = pathMap.get(pointerID);
            Point point = previousPointMap.get(pointerID);

            // calculate how far the user moved from the last update
            float deltaX = Math.abs(newX - point.x);
            float deltaY = Math.abs(newY - point.y);

            // if the distance is significant enough to matter
            if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {

               // move the path to the new location
               path.quadTo(point.x, point.y, (newX + point.x) / 2,
                       (newY + point.y) / 2);

               // store the new coordinates
               point.x = (int) newX;
               point.y = (int) newY;


            }
         }
      }
   }

   // called when the user drags along the screen
   private void touchMoved(MotionEvent event) {
      // for each of the pointers in the given MotionEvent

      for (int i = 0; i < event.getPointerCount(); i++) {

         Log.i("PRESSURE", event.getSize()+"");
         // get the pointer ID and pointer index
         int pointerID = event.getPointerId(i);
         int pointerIndex = event.findPointerIndex(pointerID);

         // if there is a path associated with the pointer
         if (pathMap.containsKey(pointerID)) {

            // get the new coordinates for the pointer
            float newX = event.getX(pointerIndex);
            float newY = event.getY(pointerIndex);

            // get the path and previous point associated with
            // this pointer
            Path path = pathMap.get(pointerID);
            Point point = previousPointMap.get(pointerID);

            // calculate how far the user moved from the last update
            float deltaX = Math.abs(newX - point.x);
            float deltaY = Math.abs(newY - point.y);

            // if the distance is significant enough to matter
            if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {

               // move the path to the new location
               path.quadTo(point.x, point.y, (newX + point.x) / 2,
                       (newY + point.y) / 2);

               // store the new coordinates
               point.x = (int) newX;
               point.y = (int) newY;


            }
         }
      }
   }

   // called when the user finishes a touch
   private void touchEnded(int lineID) {
      Path path = pathMap.get(lineID); // get the corresponding Path
      bitmapCanvas.drawPath(path, paintLine); // draw to bitmapCanvas
      path.reset(); // reset the Path
   }

   // save the current image to the Gallery
   public void saveImage() {
      // use "Doodlz" followed by current time as the image name
      final String name = "Doodlz" + System.currentTimeMillis() + ".jpg";

      // insert the image on the device
      String location = MediaStore.Images.Media.insertImage(
              getContext().getContentResolver(), bitmap, name,
              "Doodlz Drawing");

      if (location != null) {
         // display a message indicating that the image was saved
         Toast message = Toast.makeText(getContext(),
                 R.string.message_saved,
                 Toast.LENGTH_SHORT);
         message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                 message.getYOffset() / 2);
         message.show();
      }
      else {
         // display a message indicating that there was an error saving
         Toast message = Toast.makeText(getContext(),
                 R.string.message_error_saving, Toast.LENGTH_SHORT);
         message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                 message.getYOffset() / 2);
         message.show();
      }
   }

   // print the current image
   public void printImage() {
      if (PrintHelper.systemSupportsPrint()) {
         // use Android Support Library's PrintHelper to print image
         PrintHelper printHelper = new PrintHelper(getContext());

         // fit image in page bounds and print the image
         printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
         printHelper.printBitmap("Doodlz Image", bitmap);
      }
      else {
         // display message indicating that system does not allow printing
         Toast message = Toast.makeText(getContext(),
                 R.string.message_error_printing, Toast.LENGTH_SHORT);
         message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
                 message.getYOffset() / 2);
         message.show();
      }
   }
}

/**************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/