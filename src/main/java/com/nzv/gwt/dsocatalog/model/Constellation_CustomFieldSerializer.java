package com.nzv.gwt.dsocatalog.model;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Constellation_CustomFieldSerializer extends CustomFieldSerializer<Constellation>{
	
	@Override
	public void serializeInstance(SerializationStreamWriter streamWriter,
			Constellation instance) throws SerializationException {
		streamWriter.writeString(instance.getCode());
		streamWriter.writeString(instance.getName());
		streamWriter.writeDouble(instance.getCenterRightAscension());
		streamWriter.writeDouble(instance.getCenterDeclinaison());
		streamWriter.writeDouble(instance.getAreaInSquareDegrees());
		if (instance.getBoundaryPoints() == null) {
			streamWriter.writeObject(new ArrayList<ConstellationBoundaryPoint>());
		} else {
			streamWriter.writeObject(new ArrayList<ConstellationBoundaryPoint>(instance.getBoundaryPoints()));
		}
		if (instance.getShapeLines() == null) {
			streamWriter.writeObject(new HashSet<ConstellationShapeLine>());
		} else {
			streamWriter.writeObject(new HashSet<ConstellationShapeLine>(instance.getShapeLines()));
		}
	}
	
	public static void serialize(SerializationStreamWriter streamWriter,
			Constellation instance) throws SerializationException {
		streamWriter.writeString(instance.getCode());
		streamWriter.writeString(instance.getName());
		streamWriter.writeDouble(instance.getCenterRightAscension());
		streamWriter.writeDouble(instance.getCenterDeclinaison());
		streamWriter.writeDouble(instance.getAreaInSquareDegrees());
		if (instance.getBoundaryPoints() == null) {
			streamWriter.writeObject(new ArrayList<ConstellationBoundaryPoint>());
		} else {
			streamWriter.writeObject(new ArrayList<ConstellationBoundaryPoint>(instance.getBoundaryPoints()));
		}
		if (instance.getShapeLines() == null) {
			streamWriter.writeObject(new HashSet<ConstellationShapeLine>());
		} else {
			streamWriter.writeObject(new HashSet<ConstellationShapeLine>(instance.getShapeLines()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deserializeInstance(SerializationStreamReader streamReader,
			Constellation instance) throws SerializationException {
    	instance.setCode(streamReader.readString());
    	instance.setName(streamReader.readString());
    	instance.setCenterRightAscension(streamReader.readDouble());
    	instance.setCenterDeclinaison(streamReader.readDouble());
    	instance.setAreaInSquareDegrees(streamReader.readDouble());
    	instance.setBoundaryPoints((ArrayList<ConstellationBoundaryPoint>) streamReader.readObject());
    	instance.setShapeLines((HashSet<ConstellationShapeLine>) streamReader.readObject());
    }
	
	@SuppressWarnings("unchecked")
	public static void deserialize(SerializationStreamReader streamReader,
			Constellation instance) throws SerializationException {
    	instance.setCode(streamReader.readString());
    	instance.setName(streamReader.readString());
    	instance.setCenterRightAscension(streamReader.readDouble());
    	instance.setCenterDeclinaison(streamReader.readDouble());
    	instance.setAreaInSquareDegrees(streamReader.readDouble());
    	instance.setBoundaryPoints((ArrayList<ConstellationBoundaryPoint>) streamReader.readObject());
    	instance.setShapeLines((HashSet<ConstellationShapeLine>) streamReader.readObject());
    }
}
