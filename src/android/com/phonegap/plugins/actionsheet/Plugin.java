package com.phonegap.plugins.actionsheet;

import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;

public class Plugin extends CordovaPlugin implements OnLongClickListener {

  private CordovaContextMenu contextMenu;
  private CordovaActivity cordovaActivity;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    cordovaActivity = (CordovaActivity)cordova.getActivity();
    cordovaActivity.registerForContextMenu(webView);
    webView.setOnLongClickListener(this);
  }
  
  @Override
  public boolean execute(String action, CordovaArgs args,
      CallbackContext callbackContext) throws JSONException {
    if ("create".equals(action)) {
      contextMenu = new CordovaContextMenu(cordovaActivity, args, callbackContext);
      cordovaActivity.openContextMenu(webView);
      return true;
    }
    return false; // Returning false results in a "MethodNotFound" error.
  }
  
  @Override
  public boolean onLongClick(View v) {
    return true; // We return true, to let performLongClick() know that we
           // handled the long press.
  }
  
  @Override
  public Object onMessage(String id, Object data) {
    if (contextMenu == null) {
      // Have no menu to manage, return early
      return super.onMessage(id, data);
    }
    if (id.equals("onCreateContextMenu")) {
      Map<String, Object> params = (Map<String,Object>)data;
      ContextMenu menu = (ContextMenu)params.get("menu");
      View view = (View) params.get("view");
      ContextMenuInfo menuInfo = (ContextMenuInfo) params.get("menuInfo");
      return onCreateContextMenu(menu, view, menuInfo);
    } else if (id.equals("onContextMenuClosed")) {
      return onContextMenuClosed((Menu)data);
    } else if (id.equals("onContextItemSelected")) {
      return onContextItemSelected((MenuItem)data);
    }
    return super.onMessage(id, data);
  }

  private Boolean onContextItemSelected(MenuItem data) {
    if (contextMenu.onContextItemSelected(data)) {
      return true;
    } else {
      return null;
    }
  }

  private Boolean onContextMenuClosed(Menu data) {
    if (contextMenu.onContextMenuClosed(data)) {
      return true;
    } else {
      return null;
    }
  }

  private Boolean onCreateContextMenu(ContextMenu menu, View view,
      ContextMenuInfo menuInfo) {
    if (contextMenu.onCreateContextMenu(menu, view, menuInfo)) {
      return true;
    } else {
      return null;
    }
  }
}
