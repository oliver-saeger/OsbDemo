package saeger.oliver.osbdemo.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import saeger.oliver.osbdemo.api.CreationStatus;
import saeger.oliver.osbdemo.api.DeletionStatus;

import java.net.URI;

public class StatusUtility {

    public static ResponseEntity<Void> handleDeletionStatus(DeletionStatus deletionStatus) {
        switch (deletionStatus) {
            case DELETED -> {
                return ResponseEntity.ok().build();
            }

            case DOES_NOT_EXIST -> {
                throw new ResponseStatusException(HttpStatus.GONE, "This Service Instance does not exist.");
            }

            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went very wrong here...");
            }
        }
    }

    public static ResponseEntity<Void> handleCreationStatus(CreationStatus creationStatus) {
        switch (creationStatus) {
            case CONFLICT -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data is inconsistent with existing Entities");
            }

            case ALREADY_EXISTS -> {
                return ResponseEntity.ok().build();
            }

            case CREATED -> {
                return ResponseEntity.created(URI.create("")).build();
            }

            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went very wrong here...");
            }
        }
    }

}
