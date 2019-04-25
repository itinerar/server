package com.itinerar.entity;

public enum STATUS {
	
	PUBLIC {
	      public String toString() {
	          return "PUBLIC";
	      }
	  },

	PRIVATE {
	      public String toString() {
	          return "PRIVATE";
	      }
	  },
	
	PROTECTED {
	      public String toString() {
	          return "PROTECTED";
	      }
	  }
}
