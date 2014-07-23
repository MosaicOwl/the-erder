package com.esotericsoftware.kryonet;

public class KryoNetException extends RuntimeException {
    
    private static final long serialVersionUID = 5456313302076636961L;

    public KryoNetException() {
		super();
	}

	public KryoNetException(String message, Throwable cause) {
		super(message, cause);
	}

	public KryoNetException(String message) {
		super(message);
	}

	public KryoNetException(Throwable cause) {
		super(cause);
	}
}
