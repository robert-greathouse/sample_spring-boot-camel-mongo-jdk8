package com.ewerk.prototype.proc.export.handler;

import com.ewerk.prototype.persistence.repositories.PersonRepository;
import com.ewerk.prototype.proc.util.UsedByCamel;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This camel handler is the actual workhorse of the export camel route. It takes the given {@link
 * com.ewerk.prototype.model.Person} and writes its JSON representation to the file system.
 *
 * @author h.stolzenberg
 * @since 0.0.4
 */
@Component
public class ExportHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ExportHandler.class);

  @Autowired
  private PersonRepository personRepository;

  @UsedByCamel
  @Handler
  public void export() {
    // TODO h.stolzenberg: do the real export stuff
    LOG.info("Exporting data ...");
    personRepository.findAll().stream().parallel().forEach(person -> LOG.debug("{}", person));
  }
}
