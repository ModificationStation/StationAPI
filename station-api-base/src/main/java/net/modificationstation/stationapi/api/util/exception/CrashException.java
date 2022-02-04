package net.modificationstation.stationapi.api.util.exception;

public class CrashException extends RuntimeException {
   private final CrashReport report;

   public CrashException(CrashReport report) {
      this.report = report;
   }

   public CrashReport getReport() {
      return this.report;
   }

   public Throwable getCause() {
      return this.report.getCause();
   }

   public String getMessage() {
      return this.report.getMessage();
   }
}
