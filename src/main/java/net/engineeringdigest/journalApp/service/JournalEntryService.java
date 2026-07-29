package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JournalEntryService
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    
    @Autowired
    UserService userService;
    
    public void saveEntry(JournalEntry journalEntry, String userName)
    {
        try
        {
            User byUserName = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            byUserName.getJounalEntries().add(saved);
            userService.saveEntry(byUserName);
        } catch (Exception e)
        {
            log.error("Exception", e);
        }
    }

    public void updateEntry(JournalEntry journalEntry)
    {
        try
        {
            journalEntryRepository.save(journalEntry);
        } catch (Exception e)
        {
            log.error("Exception", e);
        }
    }

    public List<JournalEntry> getAll()
    {
     return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String userName)
    {
        User byUserName = userService.findByUserName(userName);
        byUserName.getJounalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(byUserName);
        journalEntryRepository.deleteById(id);
    }
}


//controller -----> service  ------> repository