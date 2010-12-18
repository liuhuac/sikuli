package org.sikuli.script;

import java.io.*;
import java.awt.Window;
import javax.swing.JWindow;
import java.awt.Rectangle;
import com.wapmx.nativeutils.jniloader.NativeLoader;
import com.sun.awt.AWTUtilities;

public class MacUtil implements OSUtil {

   static {
      try{
         NativeLoader.loadLibrary("MacUtil");
         System.out.println("Mac OS X utilities loaded.");
      }
      catch(IOException e){
         e.printStackTrace();
      }
   }

   public int switchApp(String appName){
      return openApp(appName);
   }

   public int openApp(String appName){
      Debug.history("openApp: \"" + appName + "\"");
      if(_openApp(appName))
         return 0;
      return -1;
   }


   public int closeApp(String appName){
      Debug.history("closeApp: \"" + appName +"\"");
      try{
         String cmd[] = {"sh", "-c", 
            "ps aux |  grep " + appName + " | awk '{print $2}' | xargs kill"};
         System.out.println("closeApp: " + appName);
         Process p = Runtime.getRuntime().exec(cmd);
         p.waitFor();
         return p.exitValue();
      }
      catch(Exception e){
         return -1;
      }
   }

   public Region getWindow(String appName, int winNum){
      long pid = getPID(appName);
      Rectangle rect = getRegion(pid, winNum);
      if(rect != null)
         return new Region(rect);
      return null;
   }

   public Region getWindow(String appName){
      return getWindow(appName, 0);
   }

   public Region getFocusedWindow(){
      Rectangle rect = getFocusedRegion();
      if(rect != null)
         return new Region(rect);
      return null;
   }

   public native void bringWindowToFront(JWindow win, boolean ignoreMouse);
   public static native boolean _openApp(String appName);
   public static native long getPID(String appName);
   public static native Rectangle getRegion(long pid, int winNum);
   public static native Rectangle getFocusedRegion();

   public void setWindowOpacity(JWindow win, float alpha){
      win.getRootPane().putClientProperty("Window.alpha", new Float(alpha));
   }

   public void setWindowOpaque(JWindow win, boolean opaque){
      AWTUtilities.setWindowOpaque(win, opaque);
   }


} 
