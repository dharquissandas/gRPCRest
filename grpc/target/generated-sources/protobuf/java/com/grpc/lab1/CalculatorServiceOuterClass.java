// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CalculatorService.proto

package com.grpc.lab1;

public final class CalculatorServiceOuterClass {
  private CalculatorServiceOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AddRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AddRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AddReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AddReply_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MultRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MultRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MultReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MultReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027CalculatorService.proto\"\"\n\nAddRequest\022" +
      "\t\n\001a\030\001 \003(\005\022\t\n\001b\030\002 \003(\005\"\025\n\010AddReply\022\t\n\001c\030\003" +
      " \003(\005\"#\n\013MultRequest\022\t\n\001a\030\001 \003(\005\022\t\n\001b\030\002 \003(" +
      "\005\"\026\n\tMultReply\022\t\n\001c\030\003 \003(\0052X\n\021CalculatorS" +
      "ervice\022\037\n\003Add\022\013.AddRequest\032\t.AddReply\"\000\022" +
      "\"\n\004Mult\022\014.MultRequest\032\n.MultReply\"\000B\021\n\rc" +
      "om.grpc.lab1P\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_AddRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_AddRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AddRequest_descriptor,
        new java.lang.String[] { "A", "B", });
    internal_static_AddReply_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_AddReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AddReply_descriptor,
        new java.lang.String[] { "C", });
    internal_static_MultRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_MultRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MultRequest_descriptor,
        new java.lang.String[] { "A", "B", });
    internal_static_MultReply_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_MultReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MultReply_descriptor,
        new java.lang.String[] { "C", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
