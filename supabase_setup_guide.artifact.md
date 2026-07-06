# Complete Supabase Setup Guide for Dentary

This guide covers all steps required to set up your Supabase project. **Please re-apply the SQL script in Section 2 to fix the signup exception.**

---

## 1. Project Initialization
1.  Go to [Supabase Dashboard](https://app.supabase.com/).
2.  Create a **New Project**.
3.  Copy your **Project URL** and **Anon Key** to your `local.properties` file.

---

## 2. Database Schema (RE-RUN THIS)
Go to the **SQL Editor** and run this exact script. It includes the fix for the `profiles` mapping error.

```sql
-- 1. Create Patients Table
create table public.patients (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references auth.users not null,
  name text not null,
  phone_number text,
  email text,
  age integer,
  address text,
  gender text,
  medical_history text,
  medical_procedure text,
  image text,
  last_visit_date timestamp with time zone,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
);

-- 2. Create Profiles Table
create table public.profiles (
  id uuid references auth.users on delete cascade primary key,
  full_name text,
  clinic_name text,
  phone_number text,
  clinic_address text,
  specialization text,
  profile_picture text,
  updated_at timestamp with time zone default now()
);

-- 3. Create Appointments Table
create table public.appointments (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references auth.users not null,
  patient_id uuid references public.patients on delete cascade not null,
  title text not null,
  description text,
  appointment_date timestamp with time zone not null,
  duration_minutes integer default 60,
  status text default 'SCHEDULED',
  treatment_notes text,
  cost numeric,
  created_at timestamp with time zone default now(),
  updated_at timestamp with time zone default now()
);

-- 4. Create Profile Trigger
-- This function correctly maps metadata 'display_name' to the 'full_name' column.
create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, full_name, clinic_name, phone_number, clinic_address)
  values (
    new.id,
    new.raw_user_meta_data->>'display_name',
    new.raw_user_meta_data->>'clinic_name',
    new.raw_user_meta_data->>'phone_number',
    new.raw_user_meta_data->>'address'
  );
  return new;
end;
$$ language plpgsql security definer;

-- Re-create the trigger
drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();

-- 5. Enable Row Level Security (RLS)
alter table public.patients enable row level security;
alter table public.profiles enable row level security;
alter table public.appointments enable row level security;

-- 6. Create RLS Policies
create policy "Users can only view their own patients." on public.patients
  for all using (auth.uid() = user_id);

create policy "Users can view their own profile." on public.profiles
  for all using (auth.uid() = id);

create policy "Users can view their own appointments." on public.appointments
  for all using (auth.uid() = user_id);
```

---

## 3. Storage Setup
1.  Go to **Storage** -> **New Bucket**.
2.  Name: `avatars`.
3.  Public: **ON**.
4.  Add these **Policies** to the `avatars` bucket:
    - **INSERT**: Target: `authenticated`, Expression: `(auth.role() = 'authenticated')`.
    - **SELECT**: Target: `public`, Expression: `true`.
    - **DELETE**: Target: `authenticated`, Expression: `(auth.role() = 'authenticated')`.

---

## 4. Auth & Email OTP
1.  Go to **Authentication** -> **Providers** -> **Email**.
2.  Enable: **ON**, Confirm email: **ON**.
3.  Go to **Auth Settings** -> Set **OTP Length** to `6`.
4.  **Email Templates**: Use the code from previous turns for "Confirm signup" and "Reset password" (ensure you use `{{ .Token }}`).

---

## 5. Troubleshooting the "JsonLiteral" Exception
This exception happens when the app tries to load a corrupted or old session from local storage.
1.  **Clear App Data**: Go to your phone settings -> Apps -> Dentary -> Clear Storage/Cache.
2.  **Restart App**: This will wipe the old session and start fresh with the new, robust session manager I implemented.
