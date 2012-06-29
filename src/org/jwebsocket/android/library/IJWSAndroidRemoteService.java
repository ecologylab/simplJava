/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: IJWSAndroidRemoteService.aidl
 */
package org.jwebsocket.android.library;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
// Declare the interface.

public interface IJWSAndroidRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.jwebsocket.android.library.IJWSAndroidRemoteService
{
private static final java.lang.String DESCRIPTOR = "org.jwebsocket.android.library.IJWSAndroidRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IJWSAndroidRemoteService interface,
 * generating a proxy if needed.
 */
public static org.jwebsocket.android.library.IJWSAndroidRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.jwebsocket.android.library.IJWSAndroidRemoteService))) {
return ((org.jwebsocket.android.library.IJWSAndroidRemoteService)iin);
}
return new org.jwebsocket.android.library.IJWSAndroidRemoteService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_open:
{
data.enforceInterface(DESCRIPTOR);
this.open();
reply.writeNoException();
return true;
}
case TRANSACTION_close:
{
data.enforceInterface(DESCRIPTOR);
this.close();
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
this.disconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_send:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.send(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendText:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendText(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_broadcastText:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.broadcastText(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendToken:
{
data.enforceInterface(DESCRIPTOR);
org.jwebsocket.android.library.ParcelableToken _arg0;
if ((0!=data.readInt())) {
_arg0 = org.jwebsocket.android.library.ParcelableToken.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.sendToken(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_saveFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _arg2;
_arg2 = (0!=data.readInt());
byte[] _arg3;
_arg3 = data.createByteArray();
this.saveFile(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_getUsername:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getUsername();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.login(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_logout:
{
data.enforceInterface(DESCRIPTOR);
this.logout();
reply.writeNoException();
return true;
}
case TRANSACTION_ping:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.ping(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getConnections:
{
data.enforceInterface(DESCRIPTOR);
this.getConnections();
reply.writeNoException();
return true;
}
case TRANSACTION_isAuthenticated:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAuthenticated();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback _arg0;
_arg0 = org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback _arg0;
_arg0 = org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.jwebsocket.android.library.IJWSAndroidRemoteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void open() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_open, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void close() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_close, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void send(java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_send, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void sendText(java.lang.String target, java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(target);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_sendText, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void broadcastText(java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_broadcastText, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void sendToken(org.jwebsocket.android.library.ParcelableToken token) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((token!=null)) {
_data.writeInt(1);
token.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_sendToken, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void saveFile(java.lang.String fileName, java.lang.String scope, boolean notify, byte[] data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(fileName);
_data.writeString(scope);
_data.writeInt(((notify)?(1):(0)));
_data.writeByteArray(data);
mRemote.transact(Stub.TRANSACTION_saveFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public java.lang.String getUsername() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUsername, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void login(java.lang.String aUsername, java.lang.String aPassword) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(aUsername);
_data.writeString(aPassword);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void logout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_logout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void ping(boolean echo) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((echo)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_ping, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void getConnections() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getConnections, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public boolean isAuthenticated() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAuthenticated, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Register a service to call back to the clients using this remote service
     */
public void registerCallback(org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Remove a previously registered callback interface.
     */
public void unregisterCallback(org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_open = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_close = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_disconnect = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_send = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_sendText = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_broadcastText = (IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_sendToken = (IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_saveFile = (IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getUsername = (IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_login = (IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_logout = (IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_ping = (IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getConnections = (IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_isAuthenticated = (IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_registerCallback = (IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_unregisterCallback = (IBinder.FIRST_CALL_TRANSACTION + 15);
}
public void open() throws android.os.RemoteException;
public void close() throws android.os.RemoteException;
public void disconnect() throws android.os.RemoteException;
public void send(java.lang.String data) throws android.os.RemoteException;
public void sendText(java.lang.String target, java.lang.String data) throws android.os.RemoteException;
public void broadcastText(java.lang.String data) throws android.os.RemoteException;
public void sendToken(org.jwebsocket.android.library.ParcelableToken token) throws android.os.RemoteException;
public void saveFile(java.lang.String fileName, java.lang.String scope, boolean notify, byte[] data) throws android.os.RemoteException;
public java.lang.String getUsername() throws android.os.RemoteException;
public void login(java.lang.String aUsername, java.lang.String aPassword) throws android.os.RemoteException;
public void logout() throws android.os.RemoteException;
public void ping(boolean echo) throws android.os.RemoteException;
public void getConnections() throws android.os.RemoteException;
public boolean isAuthenticated() throws android.os.RemoteException;
/**
     * Register a service to call back to the clients using this remote service
     */
public void registerCallback(org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback cb) throws android.os.RemoteException;
/**
     * Remove a previously registered callback interface.
     */
public void unregisterCallback(org.jwebsocket.android.library.IJWSAndroidRemoteServiceCallback cb) throws android.os.RemoteException;
}
