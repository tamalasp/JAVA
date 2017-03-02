package tamal.chowdhury.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class UploadDownLoadServiceImpl implements UploadDownLoadService{

	public static final String _UPLOAD_DIRECTORY = "E:/MY_STS/REST WEBSERVICE USING RestEASY/UPLOAD/";
	public static final String _DOWNLOAD_DIRECTORY = "E:/MY_STS/REST WEBSERVICE USING RestEASY/DOWNLOAD/";
	
	public Response uploadImageFile(MultipartFormDataInput multipartFormDataInput) {
		
		MultivaluedMap<String, String> multivaluedMap = null;
        String fileName = null;
        String uploadFilePath = null;
        
        try{
        	
        	   Map<String, List<InputPart>> map = multipartFormDataInput.getFormDataMap();
               List<InputPart> firstInputPart = map.get("uploadedFile");
               
               for(InputPart inputPart : firstInputPart){
               
               	multivaluedMap = inputPart.getHeaders();
                   fileName = getFileName(multivaluedMap);
                   
                   if(null != fileName && !"".equalsIgnoreCase(fileName)){
       	            	InputStream inputStream = inputPart.getBody(InputStream.class,null);
       	            	uploadFilePath = writeToFileServer(inputStream, fileName);
       	
       	            	inputStream.close();	
                   }
               
               }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
		
		return Response.ok("File uploaded successfully at " + uploadFilePath).build();
	}

	private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {
		
		OutputStream outputStream = null;
        String qualifiedUploadFilePath = _UPLOAD_DIRECTORY + fileName;
 
        try {
            outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally{
            //release resource, if any
            outputStream.close();
        }
        return qualifiedUploadFilePath;
    }
	

	private String getFileName(MultivaluedMap<String, String> multivaluedMap) {
		
		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");
		for (String content : contentDisposition) {
			 
            if ((content.trim().startsWith("filename"))) {
                String[] name = content.split("=");
                String exactFileName = name[1].trim().replaceAll("\"", "");
                return exactFileName;
            }
        }
        return "UnknownFileName";
	}

	public Response downloadImageFile(String fileName) {
		
		String filePath = _DOWNLOAD_DIRECTORY + fileName ;
		String contentDisposition = "attachment; filename=" + "\"" + fileName + "\"";
		
		File file = new File(filePath);
		ResponseBuilder responseBuilder = Response.status(Response.Status.OK).entity((Object)file);
		responseBuilder.header("Content-Disposition", contentDisposition);
		
		return responseBuilder.build();
	}
	

	
}
