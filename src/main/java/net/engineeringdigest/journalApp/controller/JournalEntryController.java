package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping ("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getall()
    {
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry)
    {
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("/id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId)
    {
     return journalEntryService.findById(myId).orElse(null);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateEntryById(@PathVariable ObjectId id,@RequestBody JournalEntry newEntry)
    {
        JournalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null)
        {
            old.setTitle(newEntry.getTitle() !=null &&  !newEntry.getTitle().equals("")? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() !=null && !newEntry.getContent().equals("")? newEntry.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return old;
    }

    @DeleteMapping("/id/{myId}")
    public boolean deleteEntryById(@PathVariable ObjectId myId)
    {
        journalEntryService.deleteById(myId);
        return true;
    }
}
