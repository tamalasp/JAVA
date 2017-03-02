package tamal.chowdhury.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/UploadDownLoadServiceImpl")
public interface UploadDownLoadService {

	
	@GET
    @Path("/download/image/{file_name}")
    @Produces(value={"image/png", "image/jpg", "image/gif"})
    public Response downloadImageFile(@PathParam("file_name") String fileName);
	
	
	@POST
    @Path("/upload/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImageFile(MultipartFormDataInput multipartFormDataInput);
}
