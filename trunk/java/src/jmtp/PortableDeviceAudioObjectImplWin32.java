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

import java.util.Date;


class PortableDeviceAudioObjectImplWin32 extends PortableDeviceObjectImplWin32 implements PortableDeviceAudioObject {
	
    PortableDeviceAudioObjectImplWin32(String objectID, PortableDeviceContentImplWin32 content,
            PortableDevicePropertiesImplWin32 properties) {
        
        super(objectID, content, properties);
    }

	public String getArtist() {
		return retrieveStringValue(Win32WPDDefines.WPD_MEDIA_ARTIST);
	}
	
	public String getAlbumArtist() {
		return retrieveStringValue(Win32WPDDefines.WPD_MEDIA_ALBUM_ARTIST);
	}
	
	public String getAlbum() {
		return retrieveStringValue(Win32WPDDefines.WPD_MUSIC_ALBUM);
	}
	
	public String getGenre() {
		return retrieveStringValue(Win32WPDDefines.WPD_MEDIA_GENRE);
	}

	public long getDuraction() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTitle() {
		return retrieveStringValue(Win32WPDDefines.WPD_OBJECT_NAME);
	}
	
	public Date getReleaseDate() {
		return retrieveDateValue(Win32WPDDefines.WPD_MEDIA_RELEASE_DATE);
	}
}
