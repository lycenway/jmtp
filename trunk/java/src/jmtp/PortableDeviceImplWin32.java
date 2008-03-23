/*
 * Copyright 2007 Pieter De Rycke
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

package jmtp;

import be.derycke.pieter.com.COM;
import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.COMReference;

/**
 *
 * @author Pieter De Rycke
 */
class PortableDeviceImplWin32 implements PortableDevice {
    
    private String deviceID;
    @SuppressWarnings("unused")
	private COMReference pDeviceManager;
    @SuppressWarnings("unused")
	private COMReference pDevice;
    
    PortableDeviceImplWin32(COMReference pDeviceManager, String deviceID) {
        this.pDeviceManager = pDeviceManager;
        this.deviceID = deviceID;
        try {
            pDevice = COM.CoCreateInstance(WPDImplWin32.CLSID_PortableDevice, 0, 
                    COM.CLSCTX_INPROC_SERVER, WPDImplWin32.IID_IPortableDevice);
        } 
        catch (COMException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * In c++ geïmplementeerde methoden
     */
    private native String getDeviceFriendlyName(String deviceID) throws COMException;
    private native String getDeviceManufacturer(String deviceID) throws COMException;
    private native String getDeviceDescription(String deviceID) throws COMException;
    private native void openImpl(PortableDeviceValuesImplWin32 values) throws COMException;
    private native void closeImpl() throws COMException;
    native PortableDeviceContentImplWin32 getDeviceContent() throws COMException;
    
    /*
     * In Java geïmplementeerde methoden
     */
    public String getFriendlyName() {
        try {
            return getDeviceFriendlyName(deviceID);
        }
        catch(COMException e) {
            return null;
        }
    }
    
    public String getManufacturer()  {
        try {
            return getDeviceManufacturer(deviceID);
        }
        catch(COMException e) {
            return null;
        }
    }
    
    public String getDescription()  {
        try {
            return getDeviceDescription(deviceID);
        }
        catch(COMException e) {
            return null;
        }
    }

    public void open(String appName, int appMajor, int appMinor, int appRevision) {
        try {
            PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
            values.setStringValue(Win32WPDDefines.WPD_CLIENT_NAME, appName);
            values.setUnsignedIntegerValue(Win32WPDDefines.WPD_CLIENT_MAJOR_VERSION, appMajor);
            values.setUnsignedIntegerValue(Win32WPDDefines.WPD_CLIENT_MINOR_VERSION, appMinor);
            values.setUnsignedIntegerValue(Win32WPDDefines.WPD_CLIENT_REVISION, appRevision);
            
            openImpl(values);
        }
        catch(COMException e) {
            e.printStackTrace();
        }
    }
    
    public void open() {
        try {
            openImpl(new PortableDeviceValuesImplWin32());
        }
        catch(COMException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            closeImpl();
        }
        catch(COMException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return deviceID;
    }

    public PortableDeviceObject[] getRootObjects() {
        try {
            PortableDeviceContentImplWin32 content = getDeviceContent();
            PortableDevicePropertiesImplWin32 properties = 
                    content.getProperties();

            String[] childIDs = content.listChildObjects(Win32WPDDefines.WPD_DEVICE_OBJECT_ID);
            PortableDeviceObject[] objects = new PortableDeviceObject[childIDs.length];
            for(int i = 0; i < childIDs.length; i++)
            	objects[i] = WPDImplWin32.convertToPortableDeviceObject(childIDs[i], content, properties);
            
            return objects;
        }
        catch (COMException e) {
            e.printStackTrace();
            return null;
        }
    }
}
