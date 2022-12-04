ALTER TABLE dms_sample_dbo.player
DROP FOREIGN KEY sport_team_fk;

ALTER TABLE dms_sample_dbo.seat
DROP FOREIGN KEY seat_type_fk;

ALTER TABLE dms_sample_dbo.seat
DROP FOREIGN KEY s_sport_location_fk;

ALTER TABLE dms_sample_dbo.sport_division
DROP FOREIGN KEY sd_sport_type_fk;

ALTER TABLE dms_sample_dbo.sport_division 
DROP FOREIGN KEY sd_sport_league_fk;

ALTER TABLE dms_sample_dbo.sport_league
DROP FOREIGN KEY sl_sport_type_fk;

ALTER TABLE dms_sample_dbo.sport_team 
DROP FOREIGN KEY st_sport_type_fk;

ALTER TABLE dms_sample_dbo.sport_team 
DROP FOREIGN KEY home_field_fk;

ALTER TABLE dms_sample_dbo.sporting_event
DROP FOREIGN KEY se_sport_type_fk;

ALTER TABLE dms_sample_dbo.sporting_event 
DROP FOREIGN KEY se_away_team_id_fk;

ALTER TABLE dms_sample_dbo.sporting_event 
DROP FOREIGN KEY se_home_team_id_fk;

ALTER TABLE dms_sample_dbo.sporting_event
DROP FOREIGN KEY se_location_id_fk;

ALTER TABLE dms_sample_dbo.sporting_event_ticket
DROP FOREIGN KEY set_person_id;

ALTER TABLE dms_sample_dbo.sporting_event_ticket
DROP FOREIGN KEY set_sporting_event_fk;

ALTER TABLE dms_sample_dbo.sporting_event_ticket
DROP FOREIGN KEY set_seat_fk;

ALTER TABLE dms_sample_dbo.ticket_purchase_hist 
DROP FOREIGN KEY tph_sport_event_tic_id;

ALTER TABLE dms_sample_dbo.ticket_purchase_hist 
DROP FOREIGN KEY tph_ticketholder_id;

ALTER TABLE dms_sample_dbo.ticket_purchase_hist 
DROP FOREIGN KEY tph_transfer_from_id;

drop index set_ev_id_tkholder_id_idx on dms_sample_dbo.sporting_event_ticket;
drop index set_seat_idx on dms_sample_dbo.sporting_event_ticket;
drop index set_ticketholder_idx on dms_sample_dbo.sporting_event_ticket;
drop index set_sporting_event_idx on  dms_sample_dbo.sporting_event_ticket;
