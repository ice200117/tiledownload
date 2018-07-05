/** ===========================================================
 *  GMapOffline : open source google map offline - Java
 *  ===========================================================
 *  
 *  This program use for download all required google map files 
 *  from google.maps server. This supported only google version 132e.
 *  
 *  This application is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *  License for more details.
 *  
 *  @author		JiiiY 
 *  @assistant	Topping
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class gmapdownloader {
	
	/**
	 * Static Variable
	 */
	public String app_path = System.getProperty("user.dir") + "/";
	public String mapfiles = "http://maps.google.com/mapfiles/mapfiles/132e/map2";
	
	
	
	//**************************** Function *********************************
	public int coord_to_tile_x1(int zoom,double lat,double lon){
		int xtile = (int)(Math.floor( (lon + 180.0) / 360.0 * (1<<zoom) ));
		return xtile;
	}

	public int coord_to_tile_y1(int zoom,double lat,double lon){
		double lat_r = lat * Math.PI / 180.0;
		int ytile = (int)(Math.floor( (1 - Math.log(Math.tan(lat_r) + 1 / Math.cos(lat_r)) / Math.PI) / 2 * (1<<zoom) ));
		return ytile;
	}



	public int coord_to_tile_x(int zl,double lat,double lng){

		lng += 180.0;
		int world_tiles = (int) Math.pow(2,zl);
		double x = (int)(world_tiles / 360.0 * lng);
		/*
		double tiles_pre_radian = world_tiles / (2 * Math.PI);
		double e = Math.sin(lat*(1/180 *Math.PI));
		double y = (int)(world_tiles/2 + 0.5*Math.log((1+e)/(1-e)) * (-tiles_pre_radian));
		*/
		lat += 85.0;
		double y = (world_tiles / 170.0 * lat);
		
		int yi = world_tiles - ( (int)(Math.round(y)) % world_tiles );
		
		return (int)Math.round(x) % world_tiles;
	}
	
	public int coord_to_tile_y(int zl,double lat,double lng){
		
		lng += 180.0;
		int world_tiles = (int) Math.pow(2,zl);
		double x = (int)(world_tiles / 360.0 * lng);
		/*
		double tiles_pre_radian = world_tiles / (2 * Math.PI);
		double e = Math.sin(lat*(1/180 *Math.PI));
		double y = (int)(world_tiles/2 + 0.5*Math.log((1+e)/(1-e)) * (-tiles_pre_radian));
		*/
		lat += 85.0;
		double y = (world_tiles / 170.0 * lat);
		
		int yi = world_tiles - ( (int)(Math.round(y)) % world_tiles );
		
		return yi;
	}
	
	public String downloadToString(String href) {
        
		URLConnection  conn = null;
		StringBuilder  sb   = null;
		BufferedReader bin   = null;
		
		try {
			URL url = new URL(href);
			sb = new StringBuilder();
			conn = url.openConnection();
			bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = null;
			while ((line = bin.readLine()) != null) {
				sb.append(line + "\n");
			}
			System.out.println("downloading: " + href );
			bin.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return sb.toString();
    } 
	//************************** End Utility Function *********************************
	
	public boolean outXYZ(int x, int y, int z)
	{
		File file = new File("xyz.conf");
		String js = String.valueOf(x) + "\n" + String.valueOf(y) + "\n" + String.valueOf(z) + "\n";
		System.out.println("xyz = " + js);
		try {
			BufferedWriter out = null;
			out = new BufferedWriter(new FileWriter(file));
			out.write(js);
			out.close();
			return true;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	
	public boolean download(String href,String filename)
	{
		//mkdir if folder not existed
		File file = new File(app_path+filename);
    String absolutePath = file.getAbsolutePath();
    String filePath = absolutePath.
          substring(0,absolutePath.lastIndexOf(File.separator));

		if (file.exists() && file.length() > 0) { 
			System.out.println("already downloaded: " + "./" + filename);
			return true;
		}
		
    File dir  = new File(filePath);
    if (!dir.exists()) { 
      dir.mkdirs();
    }

		URLConnection conn = null;
		OutputStream  out  = null;
		InputStream   in   = null;
		try {
			URL url = new URL(href);
			out = new BufferedOutputStream(new FileOutputStream(file));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println("downloading: " + "./" + filename + "\t" + numWritten);
			in.close();
			out.close();
			return true;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	
	public void downloadall() throws IOException{
		
//		// initial google offline dirs
//		String[] godir = {"mapfiles","tiles","mapfiles/132e/maps2","mapfiles/132e/maps2.api","mapfiles/ge/v/1/4"};
//		for(int i=0; i<godir.length; i++){
//			File dir  = new File(app_path+"/"+godir[i]);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//		}
//		
//		// download images
//		download("http://maps.google.com/mapfiles/mapcontrols2.png","mapfiles/mapcontrols2.png");
//		download("http://maps.google.com/mapfiles/transparent.png","mapfiles/transparent.png");
//		download("http://maps.google.com/mapfiles/openhand.cur","mapfiles/openhand.cur");
//		download("http://maps.google.com/mapfiles/closedhand.cur","mapfiles/closedhand.cur");
//		download("http://maps.google.com/mapfiles/ms/crosshairs.cur","mapfiles/crosshairs.cur");
//		
//		// key from tutorial - http://econym.org.uk/gmap/
//		//String map_api_key = "ABQIAAAAPDUET0Qt7p2VcSk6JNU1sBSM5jMcmVqUpI7aqV44cW1cEECiThQYkcZUPRJn9vy_TWxWvuLoOfSFBw";
//		
//		// download common js
//		//download("http://maps.google.com/maps?file=api&amp;v=2&amp;key="+map_api_key,"my_googleapi_off.js");
//		//download("http://maps.google.com/mapfiles/132e/maps2/main.js","mapfiles/132e/maps2/main.js");
//		//download("http://maps.google.com/mapfiles/132e/maps2.api/main.js","mapfiles/132e/maps2.api/main.js");
//		download("http://maps.google.com/mapfiles/132e/gc.js","mapfiles/132e/gc.js");
//		download("http://maps.google.com/mapfiles/ge/v/1/4/loader.js","mapfiles/ge/v/1/4/loader.js");
//		
//		
//		// download api js
//		download("http://maps.google.com/mapfiles/132e/maps2.api/mod_control.js","mapfiles/132e/maps2.api/mod_control.js");
//		download("http://maps.google.com/mapfiles/132e/maps2.api/mod_display_manager.js","mapfiles/132e/maps2.api/mod_display_manager.js");
//		download("http://maps.google.com/mapfiles/132e/maps2.api/mod_jslinker.js","mapfiles/132e/maps2.api/mod_jslinker.js");
//		download("http://maps.google.com/mapfiles/132e/maps2.api/mod_poly.js","mapfiles/132e/maps2.api/mod_poly.js");
//		download("http://maps.google.com/cat_js/mapfiles/132e/maps2.api/{mod_poly,mod_mspe}.js","mapfiles/132e/maps2.api/{mod_poly,mod_mspe}.js");
//		
//		
//		
//		File file;
//		BufferedWriter out = null;
//		String js = null;
//		
//		// get my_googleapi.js and transmogrify
//		//js = js.replace("/intl/en_ALL","");
//		//js = js.replace("http://maps.google.com/mapfiles/", "../gmapoffline/mapfiles/");
//	    //js = js.replace("http://mt0.google.com/mt?", "../gmapoffline/tiles/mt_");
//	    //js = js.replace("http://mt1.google.com/mt?", "../gmapoffline/tiles/mt_");
//	    //js = js.replace("http://mt2.google.com/mt?", "../gmapoffline/tiles/mt_");
//	    //js = js.replace("http://mt3.google.com/mt?", "../gmapoffline/tiles/mt_");
//		
//		// get main.js and transmogrify
//		file = new File(app_path + "mapfiles/132e/maps2/main.js");
//		if(!file.exists()){
//			js = downloadToString("http://maps.google.com/mapfiles/132e/maps2/main.js");
//		    js = js.replace( "\"&s=\",f]", "\"\",\".jpeg\"]");
//		    out = new BufferedWriter(new FileWriter(file));
//		    out.write(js);
//		    out.close();
//		}
//	    
//	    // get main2.js and transmogrify
//		file = new File(app_path + "mapfiles/132e/maps2.api/main.js");
//		if(!file.exists()){
//			js = downloadToString("http://maps.google.com/mapfiles/132e/maps2.api/main.js");
//		    js = js.replace( "\"&s=\",f]", "\"\",\".jpeg\"]");
//		    out = new BufferedWriter(new FileWriter(file));
//		    out.write(js);
//		    out.close();
//		}
//		
//	    
//	    
//		//get main.js and transmogrify
//		//js = js.replace("s=p+\"/cat_js\"+n+\"%7B\"","s=p+\"\"+n+\"%7B\"");
//		//js = js.replace( "\"&s=\",f]", "\"\",\".jpeg\"]");
//		
//		
//		//get {mod_poly,mod_mspe}.js and transmogrify
//		//js= js.replace("ms/crosshairs.cur","crosshairs.cur");
		
		
		//download map
		String ver = "w2p.87";//terrain
		for(int z=zl_min; z<=zl_max; z++){
			
			int x_min = coord_to_tile_x1(z,lat_bgn,lng_bgn);
			int y_max = coord_to_tile_y1(z,lat_bgn,lng_bgn);
			
			int x_max = coord_to_tile_x1(z,lat_end,lng_end);
			int y_min = coord_to_tile_y1(z,lat_end,lng_end);
			System.out.println("zzzzzzzzzzzzzzzzzzzz");
			System.out.println(z);
			System.out.println("xxxxxxx");
			System.out.println(x_min);
			System.out.println(x_max);
			System.out.println("yyyyyyy");
			System.out.println(y_min);
			System.out.println(y_max);
			System.out.println("************");
			//Read XYZ
			String filePath = "xyz.conf";
			List<String> list = new ArrayList<String>();
			int xb, yb, zb;
			xb=yb=zb=0;
			try {
				File file=new File(filePath);
				if(file.isFile() && file.exists()){
					InputStreamReader read = new InputStreamReader(
							new FileInputStream(file));
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while((lineTxt = bufferedReader.readLine()) != null){
						System.out.println(lineTxt);
						list.add(lineTxt);
					}
					System.out.println("*******end in xyx");
					read.close();

					xb = Integer.parseInt(  list.get(0) );
					yb = Integer.parseInt(  list.get(1) );
					zb = Integer.parseInt(  list.get(2) );
				}else{
					System.out.println("can not find file: " + filePath);
				}
			} catch (Exception e) {
				System.out.println("can not read file: " + filePath);
				e.printStackTrace();
			}
			
			if(z<zb)continue;
			for(int x=x_min; x<=x_max; x++){
					if(z==zb && x<xb)continue;
				for(int y=y_min; y<=y_max; y++){
					if(z==zb && x==xb && y<yb)continue;
					
					outXYZ(x,y,z);
					String mapparams = "x="+x+"&y="+y+"&z="+z;
					//download("http://mt"+((x+y)%4)+".google.com/mt?v="+ver+"&hl=en&"+mapparams, "tiles/mt_"+mapparams+".jpeg");
					//try{
					//	Thread.sleep(1000);
					//} catch (Exception e) {
					//	e.printStackTrace();
					//}
					//download("http://mt"+((x+y)%4)+".google.com/vt/lyrs=s&"+mapparams, "tiles/mt_"+mapparams+".jpeg");
					boolean d = false;
					//d =  download("http://mt"+((x+y)%4)+".google.com/vt/lyrs=y&"+mapparams+"&s=Ga", "tiles/yt_"+mapparams+".jpeg");
					
					//高德
					d =  download("http://webrd0"+((x+y)%4 + 1)+".is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&"+ mapparams, "tiles/yt_"+mapparams+".jpeg");
					
					if(!d){
						System.exit(0);
					}
				}
			}
		}
		//end
		System.out.println( "========================== finished ==========================");
	}
	

	//default
	public static int zl_min = 6;
	public static int zl_max = 9;
	public static double lat_bgn = 4;
	public static double lat_end = 10;
	public static double lng_bgn = 96;
	public static double lng_end = 106;
	
	public static void main(String args[]){
		
		//for(int i=0; i<args.length; i++){
		//	String arg = args[i];
		//	if(arg.indexOf("-zoom:")==0){
		//		arg = arg.replace("-zoom:","");
		//		zl_min = Integer.parseInt( arg.split("-")[0] );
		//		zl_max = Integer.parseInt( arg.split("-")[1] );
		//	}
		//	if (arg.indexOf("-lat")==0){
		//		arg = arg.replace("-lat:","");
		//		lat_bgn = Integer.parseInt( arg.split("-")[0] );
		//		lat_end = Integer.parseInt( arg.split("-")[1] );
		//	}
		//	if (arg.indexOf("-lng")==0){
		//		arg = arg.replace("-lng:","");
		//		lng_bgn = Integer.parseInt( arg.split("-")[0] );
		//		lng_end = Integer.parseInt( arg.split("-")[1] );
		//	}
		//}
		String filePath = "params.conf";
		List<String> list = new ArrayList<String>();
		try {
			File file=new File(filePath);
			if(file.isFile() && file.exists()){
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					//System.out.println(lineTxt);
					list.add(lineTxt);
				}
				read.close();
				
				lng_bgn = Double.parseDouble( list.get(0) );
				lng_end = Double.parseDouble( list.get(1) );
				lat_bgn = Double.parseDouble( list.get(2) );
				lat_end = Double.parseDouble( list.get(3) );
				zl_min = Integer.parseInt(  list.get(4) );
				zl_max = Integer.parseInt(  list.get(5) );
			}else{
				System.out.println("can not find file: " + filePath);
			}
		} catch (Exception e) {
			System.out.println("can not read file: " + filePath);
			e.printStackTrace();
		}


		gmapdownloader gmd = new gmapdownloader();
		try{
			gmd.downloadall();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
