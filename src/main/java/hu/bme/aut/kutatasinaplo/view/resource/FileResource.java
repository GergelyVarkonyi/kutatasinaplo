package hu.bme.aut.kutatasinaplo.view.resource;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.service.BlobFileService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.DeleteFromListOfEntityVO;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lombok.extern.java.Log;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path("/file")
@Log
public class FileResource {

	private ExperimentService experimentService;
	private BlobFileService fileService;
	private DataViewMapper mapper;

	@Inject
	public FileResource(ExperimentService experimentService, BlobFileService fileService, DataViewMapper mapper) {
		this.experimentService = experimentService;
		this.fileService = fileService;
		this.mapper = mapper;
	}

	@POST
	@Path("/upload/attachment")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response uploadAttachments(String viewJson) {
		log.info("File upload");
		try {
			List<BlobFileVO> fileVOs = mapFileVOsFromJson(viewJson);
			int id = getExperimentIdFromJson(viewJson);

			if (experimentService.attachFiles(id, fileVOs)) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/upload/images")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response uploadImages(String viewJson) {
		log.info("File upload");
		try {
			List<BlobFileVO> fileVOs = mapFileVOsFromJson(viewJson);
			int id = getExperimentIdFromJson(viewJson);

			if (experimentService.attachImages(id, fileVOs)) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/download/attachment")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response downloadAttachment(@QueryParam("id") int id) {
		log.info("File upload");
		try {
			BlobFile file = fileService.loadById(id);
			return Response.ok(file.getData(), file.getType()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/delete/attachment")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response deleteAttachment(DeleteFromListOfEntityVO ids) {
		log.info("Attachment deleted");
		try {
			if (fileService.deleteAttachment(ids.getEntityId(), ids.getId())) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/delete/image")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response deleteImage(DeleteFromListOfEntityVO ids) {
		log.info("Image deleted");
		try {
			if (fileService.deleteImage(ids.getEntityId(), ids.getId())) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	private Integer getExperimentIdFromJson(String json) throws ParseException {
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactoryImplementation();

		Map jsonObject = (LinkedHashMap) parser.parse(json, containerFactory);
		return Integer.parseInt((String) jsonObject.get("id"));
	}

	private List<BlobFileVO> mapFileVOsFromJson(String json) throws ParseException {
		List<BlobFileVO> fileVOs = Lists.newArrayList();

		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactoryImplementation();

		Map jsonObject = (LinkedHashMap) parser.parse(json, containerFactory);
		Iterator fileIterator = ((List) jsonObject.get("list")).listIterator();
		System.out.println("==iterate result==");
		while (fileIterator.hasNext()) {
			// Object entry = fileIterator.next();
			Map file = (LinkedHashMap) fileIterator.next();
			String name = (String) file.get("name");
			String type = (String) file.get("type");
			Long size = (Long) file.get("size");
			String data = ((String) file.get("data")).split(",")[1];
			fileVOs.add(BlobFileVO.builder()
					.name(name)
					.type(type)
					.size(size)
					.data(data)
					.build());

		}

		return fileVOs;
	}

	private final class ContainerFactoryImplementation implements ContainerFactory {
		@Override
		public List creatArrayContainer() {
			return new LinkedList();
		}

		@Override
		public Map createObjectContainer() {
			return new LinkedHashMap();
		}
	}
}
