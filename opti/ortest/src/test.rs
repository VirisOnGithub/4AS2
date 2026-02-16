use crate::flpb_parser::{FlpbInstance, parse_flpb_file};

pub fn demo() -> Result<FlpbInstance, Box<dyn std::error::Error>> {
    let data = parse_flpb_file("RhoneCities_500.flpb")?;
    // println!(
    //     "name={} facilities={} customers={}",
    //     data.name, data.nb_facilities, data.nb_customers
    // );
    // println!("facilities: {:?}", data.facilities);
    // println!("customers: {:?}", data.customers);
    Ok(data)
}
